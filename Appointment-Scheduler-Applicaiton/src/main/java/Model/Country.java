package Model;

public class Country {

    private int countryID;
    private String countryName;


    public Country(int countryID, String countryName){
        this.countryName = countryName;
        this.countryID = countryID;
    }

    public int getCountryID(){
        return countryID;
    }

    public String getCountryName(){
        return countryName;
    }
}
