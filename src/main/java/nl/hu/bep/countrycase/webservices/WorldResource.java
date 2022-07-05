package nl.hu.bep.countrycase.webservices;

import nl.hu.bep.countrycase.model.Country;
import nl.hu.bep.countrycase.model.World;

import javax.json.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.StringReader;
import java.util.List;

@Path("countries")
public class WorldResource {
    private JsonObject countryToJsonObject(Country country){
        JsonObjectBuilder job = Json.createObjectBuilder();

        job.add("code", country.getCode());
        job.add("iso3", country.getIso3());
        job.add("name", country.getName());
        job.add("continent", country.getContinent());
        job.add("capital", country.getCapital());
        job.add("region", country.getRegion());
        job.add("surface", country.getSurface());
        job.add("population", country.getPopulation());
        job.add("goverment", country.getGovernment());
        job.add("lat", country.getLatitude());
        job.add("lng", country.getLongitude());

        return job.build();
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getCountries(){
        List<Country> countries = World.getWorld().getAllCountries();
        JsonArrayBuilder jab = Json.createArrayBuilder();

        for(Country country : countries){
            jab.add(countryToJsonObject(country));
        }
        return jab.build().toString();
    }
    @GET
    @Path("{countrycode}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCountry(@PathParam("countrycode") String cd){
        Country country =  World.getWorld().getCountryByCode(cd);
        return countryToJsonObject(country).toString();
    }

    @GET
    @Path("largestsurfaces")
    @Produces(MediaType.APPLICATION_JSON)
    public String getlargestsurfaces(){
        List<Country> countries = World.getWorld().get10LargestSurfaces();
        JsonArrayBuilder jab = Json.createArrayBuilder();

        for(Country country : countries){
            jab.add(countryToJsonObject(country));
        }
        return jab.build().toString();
    }

    @GET
    @Path("largestpopulations")
    @Produces(MediaType.APPLICATION_JSON)
    public String getlargestpopulations(){
        List<Country> countries = World.getWorld().get10LargestPopulations();
        JsonArrayBuilder jab = Json.createArrayBuilder();

        for(Country country : countries){
            jab.add(countryToJsonObject(country));
        }
        return jab.build().toString();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String addCountryToWorld(String jsonBody) {
        StringReader stringReader = new StringReader(jsonBody);
        JsonReader jsonReader = Json.createReader(stringReader);

        JsonObject jsonObject = jsonReader.readObject();

        String cd = jsonObject.getString("code");
        String iso = jsonObject.getString("iso3");
        String name = jsonObject.getString("name");
        String cap = jsonObject.getString("capital");
        String ct = jsonObject.getString("continent");
        String reg = jsonObject.getString("region");
        double sur = Double.parseDouble(jsonObject.getString("surface"));
        int pop = Integer.parseInt(jsonObject.getString("population"));
        String gov = jsonObject.getString("goverment");
        double lat = Double.parseDouble(jsonObject.getString("latitude"));
        double lon = Double.parseDouble(jsonObject.getString("longitude"));

        Country newCountry = new Country(cd, iso, name, cap, ct, reg, sur, pop, gov, lat, lon);
        JsonObjectBuilder job = null;
        if (World.getWorld().addCountry(newCountry)) {
            job = Json.createObjectBuilder();

            job.add("result", "succes");
        } else {
            job.add("result", "failed");
        }

        return job.build().toString();

    }

}
