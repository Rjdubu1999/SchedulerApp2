package Data_Access_Object;

import Model.Country;
import Utilities.DataBaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryDAO extends Country {


    public CountryDAO(int countryID, String countryName) {
        super(countryID, countryName);
    }

    public static ObservableList<CountryDAO> getCountryList() throws SQLException{
        ObservableList<CountryDAO> countryDAOObservableList = FXCollections.observableArrayList();
        String query = "SELECT Country_ID, Country from countries";
        PreparedStatement preparedStatement = DataBaseConnection.getConnection().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            int countryID = resultSet.getInt("Country_ID");
            String countryName = resultSet.getString("Country");
            CountryDAO country = new CountryDAO(countryID, countryName);
            countryDAOObservableList.add(country);
        }
        return countryDAOObservableList;
    }
}
