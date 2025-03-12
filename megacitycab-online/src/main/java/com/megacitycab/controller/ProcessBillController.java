package com.megacitycab.controller;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.megacitycab.model.Bill;
import com.megacitycab.model.Booking;
import com.megacitycab.model.User;
import com.megacitycab.service.BillService;
import com.megacitycab.service.BookingService;

public class ProcessBillController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BookingService bookingService;
	private BillService billService;

	@Override
	public void init() throws ServletException{
		bookingService = BookingService.getInstance();
		billService =BillService.getInstance();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
        User loggedInUser = (User) session.getAttribute("loggedInUser");
		if (loggedInUser == null) {
		    response.sendRedirect(request.getContextPath() + "/index.jsp");
		    return;
		}
		try {
			List<Booking> bookings = bookingService.getAllBookings();
			request.setAttribute("BookingList", bookings);
			System.out.println("Fetching Booking success");
			request.getRequestDispatcher("/WEB-INF/views/BillingForm.jsp").forward(request, response);
		} catch (SQLException e) {
			 System.out.println("Fetching Booking Failed");
			e.printStackTrace();
		}


	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Booking booking=bookingService.getBookingById(Integer.parseInt(request.getParameter("bookingId")));

		double baseRate = Double.parseDouble(request.getParameter("baseRate"));
		double discountAmount = Double.parseDouble(request.getParameter("discountAmount"));
		double totalFare = Double.parseDouble(request.getParameter("totalFare"));
		double taxAmount =0.10;

		Bill bill = new Bill(booking,baseRate, discountAmount, taxAmount, totalFare);
		try {
			billService.addBill(bill);
			generateReceipt(response,booking,totalFare,request);
		} catch (SQLException e) {

			e.printStackTrace();
		}


    }

	public static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }
	public void generateReceipt(HttpServletResponse response, Booking booking, Double totalFare,HttpServletRequest request) throws IOException {

	    PdfWriter writer = new PdfWriter(response.getOutputStream());
	    PdfDocument pdf = new PdfDocument(writer);
	    Document document = new Document(pdf, PageSize.A4);
	    document.setMargins(36, 36, 36, 36);


	    DeviceRgb headerColor = new DeviceRgb(50, 50, 50);
	    DeviceRgb accentColor = new DeviceRgb(0, 123, 255);


	    Table headerTable = new Table(com.itextpdf.layout.property.UnitValue.createPercentArray(new float[]{70, 30}));
	    headerTable.setWidth(com.itextpdf.layout.property.UnitValue.createPercentValue(100));


	    Cell logoCell = new Cell();
	    logoCell.setBorder(Border.NO_BORDER);



	    Paragraph companyName = new Paragraph("Mega City Cabs")
	            .setFontSize(22)
	            .setBold()
	            .setFontColor(headerColor);

	    Paragraph tagline = new Paragraph("Ride in Style, Arrive in Comfort")
	            .setFontSize(10)
	            .setFontColor(headerColor);

	    logoCell.add(companyName);
	    logoCell.add(tagline);
	    headerTable.addCell(logoCell);

	    // Receipt info
	    Cell receiptInfoCell = new Cell();
	    receiptInfoCell.setBorder(Border.NO_BORDER);
	    receiptInfoCell.setTextAlignment(com.itextpdf.layout.property.TextAlignment.RIGHT);

	    Paragraph receiptTitle = new Paragraph("RECEIPT")
	            .setFontSize(18)
	            .setBold()
	            .setFontColor(accentColor);

	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	    String currentDate = dateFormat.format(new Date());

	    Paragraph dateInfo = new Paragraph("Date: " + currentDate)
	            .setFontSize(10);

	    Paragraph bookingId = new Paragraph("Booking ID: " + booking.getBookingNumber())
	            .setFontSize(10);

	    receiptInfoCell.add(receiptTitle);
	    receiptInfoCell.add(dateInfo);
	    receiptInfoCell.add(bookingId);
	    headerTable.addCell(receiptInfoCell);

	    document.add(headerTable);


	    SolidBorder separator = new SolidBorder(accentColor, 1);
	    Paragraph separatorPara = new Paragraph(" ")
	            .setBorderBottom(separator)
	            .setMarginTop(10)
	            .setMarginBottom(10);
	    document.add(separatorPara);


	    Table customerTable = new Table(com.itextpdf.layout.property.UnitValue.createPercentArray(new float[]{30, 70}));
	    customerTable.setWidth(com.itextpdf.layout.property.UnitValue.createPercentValue(100));


	    addTableRow(customerTable, "Customer:", booking.getCustomer().getName(), false);
	    addTableRow(customerTable, "Phone:", booking.getCustomer().getMobilenumber(), false);
	    addTableRow(customerTable, "Email:", booking.getCustomer().getEmail(), false);

	    document.add(customerTable);


	    Paragraph rideDetailsHeader = new Paragraph("Ride Details")
	            .setFontSize(14)
	            .setBold()
	            .setMarginTop(15)
	            .setMarginBottom(10);
	    document.add(rideDetailsHeader);

	    Table rideTable = new Table(com.itextpdf.layout.property.UnitValue.createPercentArray(new float[]{30, 70}));
	    rideTable.setWidth(com.itextpdf.layout.property.UnitValue.createPercentValue(100));


	    addTableRow(rideTable, "Cab Category:", booking.getCab().getCategory().toString(), false);
	    addTableRow(rideTable, "Pickup:", booking.getPickupLocation(), false);
	    addTableRow(rideTable, "Destination:", booking.getDestination(), false);
	    addTableRow(rideTable, "Date & Time:", formatDateTime(booking.getBookingDateTime()), false);

	    document.add(rideTable);


	    Paragraph paymentHeader = new Paragraph("Payment Summary")
	            .setFontSize(14)
	            .setBold()
	            .setMarginTop(15)
	            .setMarginBottom(10);
	    document.add(paymentHeader);

	    Table paymentTable = new Table(com.itextpdf.layout.property.UnitValue.createPercentArray(new float[]{70, 30}));
	    paymentTable.setWidth(com.itextpdf.layout.property.UnitValue.createPercentValue(100));


	    double baseRate = Double.parseDouble(request.getParameter("baseRate"));
	    addTableRow(paymentTable, "Base Fare:", "Rs" + String.format("%.2f", baseRate), true);




	    double tax = totalFare * 0.10;
	    addTableRow(paymentTable, "Tax (10%):", "Rs" + String.format("%.2f", tax), true);


	    Cell totalLabelCell = new Cell();
	    totalLabelCell.setBorder(Border.NO_BORDER);
	    totalLabelCell.setBorderTop(new SolidBorder(ColorConstants.BLACK, 1));
	    totalLabelCell.setPaddingTop(5);
	    totalLabelCell.add(new Paragraph("Total Amount:").setBold().setTextAlignment(TextAlignment.RIGHT));
	    paymentTable.addCell(totalLabelCell);

	    Cell totalValueCell = new Cell();
	    totalValueCell.setBorder(Border.NO_BORDER);
	    totalValueCell.setBorderTop(new SolidBorder(ColorConstants.BLACK, 1));
	    totalValueCell.setPaddingTop(5);
	    totalValueCell.add(new Paragraph("Rs" + totalFare).setBold().setFontColor(accentColor).setTextAlignment(TextAlignment.RIGHT));
	    paymentTable.addCell(totalValueCell);

	    document.add(paymentTable);

	    // Footer
	    Paragraph footer = new Paragraph("Thank you for choosing our services!")
	            .setFontSize(10)
	            .setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER)
	            .setMarginTop(30);
	    document.add(footer);

	    Paragraph contactInfo = new Paragraph("For any inquiries, please contact us at support@megacitycabs.com or +1-800-CAB-RIDE")
	            .setFontSize(8)
	            .setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER)
	            .setMarginTop(5);
	    document.add(contactInfo);

	    document.close();
	}

	private void addTableRow(Table table, String label, String value, boolean rightAlign) {
	    Cell labelCell = new Cell();
	    labelCell.setBorder(Border.NO_BORDER);
	    labelCell.add(new Paragraph(label).setFontSize(10));
	    table.addCell(labelCell);

	    Cell valueCell = new Cell();
	    valueCell.setBorder(Border.NO_BORDER);
	    Paragraph valuePara = new Paragraph(value).setFontSize(10);
	    if (rightAlign) {
	        valuePara.setTextAlignment(com.itextpdf.layout.property.TextAlignment.RIGHT);
	    }
	    valueCell.add(valuePara);
	    table.addCell(valueCell);
	}

	private String formatDateTime(Date dateTime) {
	    SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy, hh:mm a");
	    return format.format(dateTime);
	}


}
