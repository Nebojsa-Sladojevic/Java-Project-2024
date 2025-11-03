package ui.windows;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import program.RentalService;
import ui.UIParameters;
import ui.buttons.FinancialStatsButton;
import ui.buttons.ShowTableFinancialButton;
import ui.buttons.StartSimulationButton;
import ui.buttons.UsersRentButton;
import ui.buttons.VehiclesButton;
import ui.panels.MapGrid;
import ui.panels.StatusPanel;
import ui.panels.StatusSemaphore;
import world.Bicycle;
import world.Car;
import world.FinancialReport;
import world.RentReport;
import world.RentRequest;
import world.Scooter;
import world.User;
import world.Vehicle;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	public MainWindow() {

		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Simulation is ready");

		JPanel topPanel = new JPanel();
		JPanel bottomPanel = new JPanel();
		MapGrid middlePanel = new MapGrid();

		int buttonGap = UIParameters.Dimensions.GAP_BETWEEN_BUTTONS;
		FlowLayout topLayout = new FlowLayout(FlowLayout.LEFT, buttonGap, buttonGap);
		topPanel.setLayout(topLayout);
		topPanel.setBackground(UIParameters.Colors.SECONDARY_BACKGROUND);
		topPanel.setBorder(UIParameters.Borders.BORDER_AROUND_BUTTONS);

		BoxLayout bottomLayout = new BoxLayout(bottomPanel, BoxLayout.Y_AXIS);
		FlowLayout bottomSubLayout = new FlowLayout(FlowLayout.LEFT, buttonGap, buttonGap);
		JPanel bottomVehiclesPanel = new JPanel(bottomSubLayout);
		JPanel bottomUsersPanel = new JPanel(bottomSubLayout);
		bottomVehiclesPanel.setBorder(UIParameters.Borders.BORDER_AROUND_BUTTONS_TOP);
		bottomUsersPanel.setBorder(UIParameters.Borders.BORDER_AROUND_BUTTONS_BOTTOM);
		bottomVehiclesPanel.setOpaque(false);
		bottomUsersPanel.setOpaque(false);
		bottomPanel.setLayout(bottomLayout);
		bottomPanel.setBackground(UIParameters.Colors.SECONDARY_BACKGROUND);
		for (StatusPanel statusPanel : RentalService.getVehicleStatusPanels())
			bottomVehiclesPanel.add(statusPanel);
		for (StatusPanel statusPanel : RentalService.getUsersStatusPanels())
			bottomUsersPanel.add(statusPanel);
		bottomPanel.add(bottomVehiclesPanel);
		bottomPanel.add(bottomUsersPanel);

		this.add(topPanel, BorderLayout.NORTH);
		this.add(middlePanel, BorderLayout.CENTER);
		this.add(bottomPanel, BorderLayout.SOUTH);

		StatusSemaphore statusSemaphore = new StatusSemaphore();
		StartSimulationButton startSimulationButton = new StartSimulationButton("Start simulation", null);
		Runnable simulationThread = () -> {
			startSimulationButton.setEnabled(false);
			RentalService.startSimulation(middlePanel.getMapGrid(), this::setTitle, statusSemaphore);
		};
		startSimulationButton.addActionListener(e -> new Thread(simulationThread, "Simulation thread").start());

		topPanel.add(statusSemaphore);
		topPanel.add(startSimulationButton);
		topPanel.add(new VehiclesButton("All vehicles", Vehicle.TABLE_HEADER, RentalService::getVehicles,
				v -> Vehicle.toTableRow((Vehicle) v)));
		topPanel.add(new VehiclesButton("Cars", Car.TABLE_HEADER, RentalService::getCars));
		topPanel.add(new VehiclesButton("Bicycles", Bicycle.TABLE_HEADER, RentalService::getBicycles));
		topPanel.add(new VehiclesButton("Scooters", Scooter.TABLE_HEADER, RentalService::getScooters));
		topPanel.add(new UsersRentButton("Rent requests", RentRequest.TABLE_HEADER, RentalService::getRentRequests));
		topPanel.add(new UsersRentButton("Users", User.TABLE_HEADER, RentalService::getUsers));
		topPanel.add(new UsersRentButton("Rent history", RentReport.TABLE_HEADER, RentalService::getRentReports));
		topPanel.add(new UsersRentButton("Breakdowns", Vehicle.BreakDown.TABLE_HEADER, RentalService::getBreakDowns));
		topPanel.add(new FinancialStatsButton("Financial total", RentalService::generateFinancialSummary));
		topPanel.add(new ShowTableFinancialButton("Financial daily", FinancialReport.TABLE_HEADER,
				RentalService::getFinancialDailyReports));
		topPanel.add(new ShowTableFinancialButton("Income vehicles", Vehicle.INCOME_TABLE_HEADER,
				RentalService::generateVehicleIncomeData));
		topPanel.add(new ShowTableFinancialButton("Income cars", Vehicle.INCOME_TABLE_HEADER,
				RentalService::generateCarsIncomeData));
		topPanel.add(new ShowTableFinancialButton("Income bicycles", Vehicle.INCOME_TABLE_HEADER,
				RentalService::generateBicyclesIncomeData));
		topPanel.add(new ShowTableFinancialButton("Income scooters", Vehicle.INCOME_TABLE_HEADER,
				RentalService::generateScootersIncomeData));
		this.pack();
		this.setMinimumSize(this.getSize());

	}

}
