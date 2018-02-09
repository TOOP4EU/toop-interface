package eu.toop.dc;

import static spark.Spark.*;

public class DCInterface {
    public static void main(String[] args) {
        get("/dc-data-response", (req, res) -> "An endpoint that receives data");
    }
}
