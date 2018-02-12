package eu.toop.iface;

import static spark.Spark.*;

public class MainInterface {
    public static void main(String[] args) {
        get("/dc-data-response", (req, res) -> "An endpoint that receives data");
    }
}
