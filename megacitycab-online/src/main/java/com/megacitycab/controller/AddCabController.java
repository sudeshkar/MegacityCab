package com.megacitycab.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.megacitycab.model.Cab;
import com.megacitycab.model.CabCategory;
import com.megacitycab.model.CabStatus;
import com.megacitycab.model.Driver;
import com.megacitycab.service.CabService;
import com.megacitycab.service.DriverService;
import com.megacitycab.utils.SessionUtils;

public class AddCabController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CabService cabService;
    private DriverService driverService;

    @Override
	public void init() throws ServletException {
        cabService = CabService.getInstance();
        driverService = DriverService.getInstance();
    }

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!SessionUtils.isUserLoggedIn(request)) {
            response.sendRedirect("index.jsp");
            return;
        }
		String action = request.getParameter("action");

        if (action == null) {
            action = "viewCabs";
        }

        switch (action) {
            case "viewCabs":
                viewCabs(request, response);
                break;
            case "editCab":
                editCab(request, response);
                break;
            case "deleteCab":
                deleteCab(request, response);
                break;
            default:
                viewCabs(request, response);
        }

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String action = request.getParameter("action");

        if (action == null) {
            // Default to view if no action specified
            viewCabs(request, response);
            return;
        }

        switch (action) {
            case "addCab":
                addCab(request, response);
                break;
            case "updateCab":
                updateCab(request, response);
                break;
            default:
                viewCabs(request, response);
        }
	}

	private void viewCabs(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Cab> cabs = cabService.getAllCabs();
        List<Driver> drivers = driverService.getAllDrivers();

        request.setAttribute("cabs", cabs);
        request.setAttribute("drivers", drivers);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/ManageCab.jsp");
        dispatcher.forward(request, response);
    }

	private void editCab(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cabIDStr = request.getParameter("cabID");

        if (cabIDStr != null && !cabIDStr.isEmpty()) {
            try {
                int cabID = Integer.parseInt(cabIDStr);
                Cab cab = cabService.getCabByCabID(cabID);
                System.out.println(cab.getDriver().getDriverID());

                if (cab != null) {
                	System.out.println("Cab is Not Null");
                    List<Cab> cabs = cabService.getAllCabs();
                    if (cabs!= null) {
                    	System.out.println("Cabs is Not Null");
					}
                    else {
                    	System.out.println("Cabs is Null");
                    }
                    List<Driver> drivers = driverService.getAllDrivers();
                    if (drivers!= null) {
                    	System.out.println("drivers is Not null" );

					}
                    else {
                    	System.out.println("drivers is null");
                    }

                    request.setAttribute("cabs", cabs);
                    request.setAttribute("drivers", drivers);
                    request.setAttribute("cabToEdit", cab);

                    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/ManageCab.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                // Handle invalid ID format
                setErrorMessage(request, "Invalid cab ID format");
            } catch (Exception e) {
                setErrorMessage(request, "Error retrieving cab: " + e.getMessage());
            }
        } else {
            setErrorMessage(request, "Cab ID is required");
        }

        response.sendRedirect(request.getContextPath() + "/AddCabController?action=viewCabs");
    }

	private void addCab(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        try {
            Cab cab = extractCabFromRequest(request, 0);


            cab.setLastUpdated(LocalDateTime.now());

            boolean success = cabService.addCab(cab);

            if (success) {
                setSuccessMessage(request, "Cab added successfully");
            } else {
                setErrorMessage(request, "Failed to add cab");
            }
        } catch (Exception e) {
            setErrorMessage(request, "Error adding cab: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/AddCabController?action=viewCabs");
    }
	private void updateCab(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cabIDStr = request.getParameter("cabID");

        if (cabIDStr != null && !cabIDStr.isEmpty()) {
            try {
                int cabID = Integer.parseInt(cabIDStr);
                Cab cab = extractCabFromRequest(request, cabID);

                cab.setLastUpdated(LocalDateTime.now());

                boolean success = cabService.updateCab(cab);

                if (success) {
                    setSuccessMessage(request, "Cab updated successfully");
                } else {
                    setErrorMessage(request, "Failed to update cab");
                }
            } catch (NumberFormatException e) {
                setErrorMessage(request, "Invalid cab ID format");
            } catch (Exception e) {
                setErrorMessage(request, "Error updating cab: " + e.getMessage());
            }
        } else {
            setErrorMessage(request, "Cab ID is required for update");
        }

        response.sendRedirect(request.getContextPath() + "/AddCabController?action=viewCabs");
    }

    private void deleteCab(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cabIDStr = request.getParameter("cabID");

        if (cabIDStr != null && !cabIDStr.isEmpty()) {
            try {
                int cabID = Integer.parseInt(cabIDStr);

                // Check if cab exists
                Cab cab = cabService.getCabByCabID(cabID);
                if (cab == null) {
                    setErrorMessage(request, "Cab not found");
                    response.sendRedirect(request.getContextPath() + "/AddCabController?action=viewCabs");
                    return;
                }

                boolean hasActiveBookings = cabService.hasActiveCabBookings(cabID);
                if (hasActiveBookings) {
                    setErrorMessage(request, "Cannot delete cab with active bookings");
                    response.sendRedirect(request.getContextPath() + "/AddCabController?action=viewCabs");
                    return;
                }

                boolean success = cabService.deleteCab(cabID);

                if (success) {
                    setSuccessMessage(request, "Cab deleted successfully");
                } else {
                    setErrorMessage(request, "Failed to delete cab");
                }
            } catch (NumberFormatException e) {
                setErrorMessage(request, "Invalid cab ID format");
            } catch (Exception e) {
                setErrorMessage(request, "Error deleting cab: " + e.getMessage());
            }
        } else {
            setErrorMessage(request, "Cab ID is required for deletion");
        }

        response.sendRedirect(request.getContextPath() + "/AddCabController?action=viewCabs");
    }

    private Cab extractCabFromRequest(HttpServletRequest request, int cabID) {
        String vehicleNumber = request.getParameter("vehicleNumber");
        String model = request.getParameter("model");
        String categoryStr = request.getParameter("category");
        String capacityStr = request.getParameter("capacity");
        String currentLocation = request.getParameter("currentLocation");
        String statusStr = request.getParameter("status");
        String driverIDStr = request.getParameter("driverID");

        int capacity = 4; // Default capacity
        if (capacityStr != null && !capacityStr.isEmpty()) {
            try {
                capacity = Integer.parseInt(capacityStr);
            } catch (NumberFormatException e) {

            }
        }

        int driverID = 0;
        if (driverIDStr != null && !driverIDStr.isEmpty()) {
            try {
                driverID = Integer.parseInt(driverIDStr);
            } catch (NumberFormatException e) {

            }
        }

        CabCategory category = null;
        CabStatus status = null;

        try {
            if (categoryStr != null) {
                category = CabCategory.valueOf(categoryStr.toUpperCase());
            }
            if (statusStr != null) {
                status = CabStatus.valueOf(statusStr.toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid category or status value: " + categoryStr + ", " + statusStr);
        }

        // Fetch Driver object using driverID
        Driver driver = null;
        if (driverID > 0) {

            driver = driverService.getDriverByID(driverID);
        }

        Cab cab = new Cab();
        cab.setCabID(cabID);
        cab.setVehicleNumber(vehicleNumber);
        cab.setModel(model);
        cab.setCategory(category);
        cab.setCapacity(capacity);
        cab.setCurrentLocation(currentLocation);
        cab.setCabStatus(status);
        cab.setDriver(driver);

        return cab;
    }

    private void setSuccessMessage(HttpServletRequest request, String message) {
        HttpSession session = request.getSession();
        session.setAttribute("message", message);
    }

    private void setErrorMessage(HttpServletRequest request, String message) {
        HttpSession session = request.getSession();
        session.setAttribute("error", message);
    }
}
