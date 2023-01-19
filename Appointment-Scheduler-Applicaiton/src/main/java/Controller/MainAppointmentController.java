package Controller;

import Data_Access_Object.AppointmentDAO;
import Data_Access_Object.ContactDAO;
import Data_Access_Object.CustomerDAO;
import Data_Access_Object.USERDAO;
import Model.*;
import Utilities.DataBaseConnection;
import com.example.wilkinson_c195.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import static Utilities.Time.convertTOUTC;


public class MainAppointmentController implements Initializable {

    @FXML private TextField AptIDField;
    @FXML private TextField UserIDField ;
    @FXML private TextField LocationField;
    @FXML private TextField TitleField;
    @FXML private TextField TypeField;
    @FXML private TextField DescriptionField;
    @FXML private TextField CustomerIDField;
    @FXML private TextField ContactField;
    @FXML private RadioButton AllAptRadio;
    @FXML private RadioButton WeeklyRadio;
    @FXML private RadioButton MonthlyRadio;
    @FXML private ComboBox<String> StartTimeCombo;
    @FXML private ComboBox<String> EndTimeCombo;
    @FXML private DatePicker StartDatePicker;
    @FXML private DatePicker EndDatePicker;
    @FXML private ComboBox<String> ContactCombo;
    @FXML private TableColumn<?,?> CustomerIDColumn;
    @FXML private TableColumn<?,?> AptIDColumn;
    @FXML private TableColumn<?,?> TitleColumn;
    @FXML private TableColumn<?,?> TypeColumn;
    @FXML private TableColumn<?,?> LocationColumn;
    @FXML private TableColumn<?,?> DescriptionCol;
    @FXML private TableColumn<?,?> StartCol;
    @FXML private TableColumn<?,?> EndColumn;
    @FXML private TableColumn<?,?> ContactIDColumn;
    @FXML private TableColumn<?,?> UserIDColumn;
    @FXML private
    TableView<Appointment> MainTableView;

    private boolean monthlyBool;
    private Appointment selectedAppointment;
    private Customer selectedCustomer;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)   {

        try {
            ObservableList<Appointment> allAppointments = AppointmentDAO.getAllAppointment();

        AptIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        TitleColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        DescriptionCol.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        LocationColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
        TypeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        ContactIDColumn.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        StartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        EndColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        CustomerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        UserIDColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));

        MainTableView.setItems(allAppointments);
    } catch (SQLException sqlException) {
        sqlException.printStackTrace();
    }
    }
    @FXML
    void loadAppointmentData(){
        try{
            DataBaseConnection.openConnection();
            Appointment selectedAppointment = MainTableView.getSelectionModel().getSelectedItem();

            if(selectedAppointment != null){
                ObservableList<Contact> contactObservableList = ContactDAO.getAllContacts();
                ObservableList<String> contactNameList = FXCollections.observableArrayList();
                String onScreenContactName = "";

                contactObservableList.forEach(contact -> contactNameList.add(contact.getContactName()));
                ContactCombo.setItems(contactNameList);
                for(Contact contact: contactObservableList){
                    if(selectedAppointment.getContactID() == contact.getContactID()){
                        onScreenContactName = contact.getContactName();
                    }
                }
                AptIDField.setText(String.valueOf(selectedAppointment.getAppointmentID()));
                TitleField.setText(selectedAppointment.getAppointmentTitle());
                LocationField.setText(selectedAppointment.getLocation());
                DescriptionField.setText(selectedAppointment.getDescription());
                TypeField.setText(selectedAppointment.getType());
                CustomerIDField.setText(String.valueOf(selectedAppointment.getCustomerID()));
                StartDatePicker.setValue(selectedAppointment.getStart().toLocalDate());
                EndDatePicker.setValue(selectedAppointment.getEnd().toLocalDate());
                StartTimeCombo.setValue(String.valueOf(selectedAppointment.getStart().toLocalTime()));
                EndTimeCombo.setValue(String.valueOf(selectedAppointment.getEnd().toLocalTime()));
                UserIDField.setText(String.valueOf(selectedAppointment.getUserID()));
                ContactCombo.setValue(onScreenContactName);

                ObservableList<String> timesOfAppointments = FXCollections.observableArrayList();

                LocalTime firstApt = LocalTime.MIN.plusHours(8);
                LocalTime lastApt = LocalTime.MAX.minusHours(1).minusMinutes(45);

                if(!firstApt.equals(0) || !lastApt.equals(0)){
                    while(firstApt.isBefore(lastApt)){
                        timesOfAppointments.add(String.valueOf(firstApt));
                        firstApt = firstApt.plusMinutes(15);
                    }
                }
                StartTimeCombo.setItems(timesOfAppointments);
                EndTimeCombo.setItems(timesOfAppointments);


                }
            }catch (Exception exception){
            exception.printStackTrace();
        }

        }

    public void onActionAddAppointment(ActionEvent actionEvent)throws IOException {
        Parent appointmentButton = FXMLLoader.load(Main.class.getResource("AddAppointment.fxml"));
        Scene scene = new Scene(appointmentButton);
        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();




   /** public void onActionDeleteAppointment() {
        if(MonthlyTab.isSelected()){
            monthlyBool = true;
            if(MonthlyApptTableView.getSelectionModel().getSelectedItem() != null){
                selectedAppointment = MonthlyApptTableView.getSelectionModel().getSelectedItem();
            }else {
                return;
            }
        }else {
            monthlyBool = false;
            if(WeeklyTableView.getSelectionModel().getSelectedItem() != null){
            selectedAppointment = WeeklyTableView.getSelectionModel().getSelectedItem();
        }else {
                return;
            }
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Delete Appointment");
        alert.setContentText("Would you like to delete this appointment?");
        alert.showAndWait().ifPresent((response -> {
            if(response == ButtonType.OK){
                DataBaseAppointment.deleteAppointment(selectedAppointment.getAppointmentId());
                if(monthlyBool){
                    MonthlyApptTableView.setItems(DataBaseAppointment.getMonthlyAppointments(selectedCustomer.getCustomerID()));

                }else {
                    WeeklyTableView.setItems(DataBaseAppointment.getWeeklyAppointments(selectedCustomer.getCustomerID()));
                }
            }
        }
                ));
    }

    @FXML
    public void onActionBack(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    } **/
}

    public void onActionModifyAppointment(ActionEvent actionEvent) {
        try{
            Connection connection = DataBaseConnection.openConnection();
            if(!TitleField.getText().isEmpty() && !DescriptionField.getText().isEmpty() && LocationField.getText().isEmpty() &&
            !TypeField.getText().isEmpty() && EndDatePicker.getValue() != null && StartDatePicker.getValue() !=null &&
            StartTimeCombo.getValue().isEmpty() && EndTimeCombo.getValue().isEmpty() && CustomerIDField.getText().isEmpty()){
                ObservableList<Customer> getAllCustomers = CustomerDAO.getAllCustomers(connection);
                ObservableList<Integer> CustomerIDIndex = FXCollections.observableArrayList();
                ObservableList<USERDAO> getAllUsers = USERDAO.getAllUser();
                ObservableList<Integer> UserIDIndex = FXCollections.observableArrayList();
                ObservableList<Appointment> getAllAppointments = AppointmentDAO.getAllAppointment();

                getAllCustomers.stream().map(Customer::getCustomerID).forEach(CustomerIDIndex::add);
                getAllUsers.stream().map(User::getUserID).forEach(UserIDIndex::add);

                LocalDate ldStart = StartDatePicker.getValue();
                LocalDate ldEnd = EndDatePicker.getValue();
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                LocalTime ltStart = LocalTime.parse(StartTimeCombo.getValue(), dateTimeFormatter);
                LocalTime ltEnd = LocalTime.parse(EndTimeCombo.getValue(), dateTimeFormatter);
                LocalDateTime localDateTimeStart = LocalDateTime.of(ldStart, ltStart);
                LocalDateTime localDateTimeEnd = LocalDateTime.of(ldEnd, ltEnd);
                ZonedDateTime zonedDateTimeStart = ZonedDateTime.of(localDateTimeStart, ZoneId.systemDefault());
                ZonedDateTime zonedDateTimeEnd = ZonedDateTime.of(localDateTimeEnd, ZoneId.systemDefault());
                ZonedDateTime zonedDateTimeConvertStart = zonedDateTimeStart.withZoneSameInstant(ZoneId.of("America/New_York"));
                ZonedDateTime zonedDateTimeConvertEnd = zonedDateTimeEnd.withZoneSameInstant(ZoneId.of("America/New_York"));

                if(zonedDateTimeConvertStart.toLocalDate().getDayOfWeek().getValue() == (DayOfWeek.SATURDAY.getValue()) ||
                zonedDateTimeConvertEnd.toLocalDate().getDayOfWeek().getValue() == (DayOfWeek.SUNDAY.getValue()) ||
                zonedDateTimeConvertStart.toLocalDate().getDayOfWeek().getValue() == (DayOfWeek.SUNDAY.getValue()) ||
                zonedDateTimeConvertEnd.toLocalDate().getDayOfWeek().getValue() == (DayOfWeek.SATURDAY.getValue())){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Selected Day Is Not a Business Day (Monday-Friday");
                    Optional<ButtonType> confirm = alert.showAndWait();
                    System.out.println("The day selected is not a business day");
                    return;
                }
                if(zonedDateTimeConvertStart.toLocalTime().isBefore(LocalTime.of(8,0,0)) ||
                zonedDateTimeConvertStart.toLocalTime().isAfter(LocalTime.of(22,0,0)) ||
                zonedDateTimeConvertEnd.toLocalTime().isBefore(LocalTime.of(8,0,0)) || zonedDateTimeConvertEnd.toLocalTime().isAfter(LocalTime.of(22,0,0))){
                    System.out.println("This time is outside of operating hours");
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Time is Outside of operating hours. (8am-10pm EST) " + zonedDateTimeConvertStart.toLocalTime() + "-" + zonedDateTimeConvertEnd.toLocalTime() + "EST");
                    Optional<ButtonType> confirm = alert.showAndWait();
                    return;
                }
                int newCustomerID = Integer.parseInt(CustomerIDField.getText());
                int newApptID = Integer.parseInt(AptIDField.getText());
                if(localDateTimeStart.isAfter(localDateTimeEnd)){
                    System.out.println("No end time for appointment selected");
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Appointments start time is after the end time.");
                    Optional<ButtonType> confirm = alert.showAndWait();
                    return;
                }
                if(localDateTimeStart.isEqual(localDateTimeEnd)){
                    System.out.println("Appointments start time and end time are the same.");
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Appointment start and end are the same times.");
                    Optional<ButtonType> confirm = alert.showAndWait();
                    return;

                }
                for(Appointment appointment:getAllAppointments){
                    LocalDateTime checkAppointmentStart = appointment.getStart();
                    LocalDateTime checkAppointmentEnd = appointment.getEnd();

                    if((newCustomerID == appointment.getCustomerID()) && (newApptID != appointment.getAppointmentID()) &&
                    localDateTimeStart.isAfter(checkAppointmentStart) && (localDateTimeStart.isBefore(checkAppointmentEnd))){
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "The end of the appointment overlaps with an existing appointment");
                        Optional<ButtonType> confirm = alert.showAndWait();
                        System.out.println("There is an overlap with another existing appointment.");
                        return;
                    }

                }
                String dateStart = StartDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String dateEnd = EndDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String timeStart = StartTimeCombo.getValue();
                String timeEnd = EndTimeCombo.getValue();
                String UTCStart = convertTOUTC(dateStart + " " + timeStart + ":00");
                String UTCEnd = convertTOUTC(dateEnd + " " + timeEnd + ":00");

                String query = "UPDATE appointments SET Appointment_ID= ?, Title=?, Description = ?, Location =?, Type = ?, Start = ?, End = ?, Last_Update = ?, Last_Updated_By=?, Customer_ID = ?, User_ID =?, Contact_ID=?, WHERE Appointment_ID = ?";

                DataBaseConnection.setPreparedStatement(DataBaseConnection.getConnection(), query);
                PreparedStatement preparedStatement = DataBaseConnection.getPreparedStatement();
                preparedStatement.setInt(1,Integer.parseInt(AptIDField.getText()));
                preparedStatement.setString(2,TitleField.getText());
                preparedStatement.setString(3, DescriptionField.getText());
                preparedStatement.setString(4, LocationField.getText());
                preparedStatement.setString(5, TypeField.getText());
                preparedStatement.setString(6, UTCStart);
                preparedStatement.setString(7,UTCEnd);
                preparedStatement.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
                preparedStatement.setString(9, "admin");
                preparedStatement.setInt(10, Integer.parseInt(CustomerIDField.getText()));
                preparedStatement.setInt(11, Integer.parseInt(UserIDField.getText()));
                preparedStatement.setInt(12, Integer.parseInt(ContactDAO.locateContact(ContactCombo.getValue())));
                preparedStatement.setInt(13, Integer.parseInt(AptIDField.getText()));
                preparedStatement.execute();

                ObservableList<Appointment> allApts = AppointmentDAO.getAllAppointment();
                MainTableView.setItems(allApts);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    public void onActionDeleteAppointment(ActionEvent actionEvent) {
    }

    public void onActionBack(ActionEvent actionEvent) {
    }
}
