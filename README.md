

# Automatic Irrigation System

## Running the Application.

Clone the application from GitHub, open it in `IntelliJ IDEA` and run it as an application. Depending on your IDE settings, you might need to trust the folder containing the project.

The project requires Java 21+ to run so make sure you are using one or download Amazon Corretto 21 by going to `File > Project Structure > Project > SDK`

Once you run the application, it will load seed data to H2, the in-memory database I have used to persist data used by the application. This seed data will enable you to test functionality easily. On start up of the project, the lands to be irrigated are setup to be irrigated starting 1 minute after the project is running on IntelliJ. You can follow the logs on the console to track the processing done by the system.

A number of environment variables change how the application is run. This is to aid in simulating different test cases. Below are the environment variables and the accepted values so you can change them on `application.properties` to simulate different tests

| Env Property | Description | Acceptable Values |
|--------------|-------------|-------------|
| irrigation_start_hour         | The hour to start automatic irrigation.        | 0 - 23
| irrigation_duration       | Amount of hours to do irrigation for all listed plots of land         | 1 - 24
| max_retries | Number Of times to invoke the Sensor API to start irrigation before failing the projecess | > 1 
| default_amount_of_water | The amount of water in litres to use as default if crop is not mapped on the database to amount of water required for irrigation | Any amount > 1
sensor_respond_with_failure | Use to force the inbuilt sensor API to return a failure response, mimicking Sensor being unavailable. | 1 for true and 0 for false.

## How the Application Works

The application is setup to rely on H2, an in memory database which makes the application easier to bundle up and be tested by a different engineer with minimal setup overhead. 

Automatic Irrigation is triggered by a scheduler, that runs periodically, in our case once every minute, to check if there are any irrigation's to be triggered for that particular minute of the hour. If there is no irrigation to be carried out, the application exits silently, logging a message on the same. If there is an irrigation to be carried out, the application loops through the list of land(s) and using Application Event's, sends the process to queue and asynchronously handles invoking the Sensor API to trigger the irrigation. 

Since we don't have a Sensor API exposed, the application has a dummy API exposed to act as the Sensor API. Using the property `sensor_respond_with_failure` on the `application.properties` file, you can mimic the Sensor API being down by setting the value of this property to `1`, or mimic the Sensor API being available by setting the value to `0`. This API returns static response and is used to simulate how the application would behave and allow the user to see retries as well as alerts generated from failures in automatic irrigation.

Once the irrigation is done, whether it's successful or not, a reset task runs every after a few minutes to reset the Irrigation Status of land's have been processed a while back, say 5 or 10 minutes ago, for demo purposes. This reset period is shorter for simulation purposes only but can be changed so the process runs after irrigation is complete or at the end of the day to ensure all plots of land are eligible for irrigation the following day.

Each land that is saved has a Status property. This allows us to suspend any land from automatic irrigation without having to remove the record from the system. This field accepts 2 values: `ACTIVE` for land that will be eligible for automatic irrigation and `SUSPENDED` for land that is skipped when triggering automatic scheduling.

Below is a summary of the APIs exposed by this application. The APIs can be found on the Postman collection found on the root of this application on GitHub.

### (a) Healthcheck

Exposes an actuator endpoint to check on the health of the application

### (b) List Plots of Land

Returns an array of all the plots of land and the various info for each land


### (c) Add New Plot of Land

This will add a New Plot of Land based on the provided information. If the crop on the land appears in the crop mapping then the amount of water needed to irrigate the land is automatically provided by the application. Should the crop not be part of the crop mapping, then a default value is assigned as the amount of water to be used for irrigating the land. This API triggers an asynchronous process in the background using Spring's Application Events to calculate the land with the most area and schedule those for earlier time slots for irrigation. As such if a smaller land holds an earlier irrigation time slot, using this API will change the time slots for all the parcels of land and prioritize land with the largest areas to have the earliest irrigation time slots.

### (d) Edit New Plot of Land

This allows the user to edit some of the fields related to the land. The fields are: Length, Width, Crop and Land Status.

### (e) Configure New Plot of Land

Allows the User to Configure specific details for a given plot of land if what the application automatically filled in isn't sufficient. The fields that can be edited by this API are Amount of Water and the Scheduled Irrigation Time of the land. Note that this API overrides the system allocated irrigation time slot for only that particular plot of land and doesn't trigger the time slots for the other parcels of land to be changed.

### (f) Get Alerts

This API will allow the Sensor or user to retrieve alerts about Sensors that were not available after retries, when automatic irrigation was triggered. Only alerts with `IsRead` set to false are returned.

### (g) Update Alert

This API will be used to update the `IsRead` field for an alert to true or false

## Assumptions During Development
The developed system relied on the following assumptions to achieve its functionality:
- Crops would be irrigated once per day
- Crops would automatically be irrigated every day of the week
- The amount of water required for irrigation isn't calculated based on the area of land, instead, it's assumed that the water required by a crop is the same amount irrespective of how big the plot of land is. The assumption here is that the water required is what is dispensed for a particular radius of land, thereby being a static amount.
- H2 is used sparingly as both a database and a cache, since setting up redis would have presented challenges for anyone else to get the application running on their machine.

## Predicting Amount of Water for each Crop

The project relies on a mapping of crop to water ratio. This is stored in the database. Any crop that is not mapped on the database with the amount of water for irrigation defaults to a default amount set on the `application.properties` file. The database table with the mapping is seeded with dummy data for 10 different crops to aid in simulating different test cases. The dummy crops mapping is as below

| Crop    | Amount of Water for Irrigation (litres) |
|---------|-----------------------------------------|
| Teff    | 1000                                    |
| Coffee  | 450                                     |
| Tea     | 450                                     |
| Barley  | 500                                     |
| Maize   | 100                                     |
| Wheat   | 750                                     |
| Yam     | 50                                      |
| Beans   | 100                                     |
| Cocoa   | 250                                     |
| Cassava | 50                                      |


### Application Response Codes

For simplicity, this application only uses a handful of response codes for different scenarios. Below are the application response codes used:  

| **ResponseCode** | **Description**                                  |
|------------------|--------------------------------------------------|
| 0                | The operation or action was successful           |
| 404              | The item/object does not exist in our data store |
| 500              | Generic error or failure occurred                |