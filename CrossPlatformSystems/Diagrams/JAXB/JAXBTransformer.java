package JAXB;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class JAXBTransformer {
    public static void main(String[] args){
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Document.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Document document = (Document) unmarshaller.unmarshal(
                    new File("kirill_example.xml"));

            modifyDocument(document);

            String html = generateHtml(document);

            try (PrintWriter out = new PrintWriter("output.html")) {
                out.println(html);
            }

            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(document, new File("modified.xml"));

            System.out.println("Transformation completed successfully!");

        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void modifyDocument(Document document) {
        Request request = document.getRequest();
        if (request != null) {
            request.setStatus("processed");
        }
    }

    private static String generateHtml(Document document) {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>\n")
                .append("<html>\n")
                .append("<head>\n")
                .append("    <title>XML Data Report</title>\n")
                .append("    <style>\n")
                .append("        body { font-family: Arial, sans-serif; margin: 20px; }\n")
                .append("        table { border-collapse: collapse; width: 100%; margin-bottom: 20px; }\n")
                .append("        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }\n")
                .append("        th { background-color: #f2f2f2; }\n")
                .append("        .section { margin-bottom: 30px; }\n")
                .append("        .section-title { font-size: 1.2em; font-weight: bold; margin-bottom: 10px; }\n")
                .append("        .footer { font-weight: bold; background-color: #e6e6e6; }\n")
                .append("    </style>\n")
                .append("</head>\n")
                .append("<body>\n");

        User user = document.getUser();
        if (user != null) {
            html.append("<div class=\"section\">\n")
                    .append("    <div class=\"section-title\">User Information</div>\n")
                    .append("    <table>\n")
                    .append("        <tr><th>ID</th><td>").append(user.getId()).append("</td></tr>\n")
                    .append("        <tr><th>Email</th><td>").append(user.getEmail()).append("</td></tr>\n")
                    .append("        <tr><th>Role</th><td>").append(user.getRole()).append("</td></tr>\n")
                    .append("        <tr><th>Full Name</th><td>").append(user.getFullName()).append("</td></tr>\n")
                    .append("    </table>\n")
                    .append("</div>\n");
        }

        Request request = document.getRequest();
        if (request != null) {
            html.append("<div class=\"section\">\n")
                    .append("    <div class=\"section-title\">Request Information</div>\n")
                    .append("    <table>\n")
                    .append("        <tr><th>ID</th><td>").append(request.getId()).append("</td></tr>\n")
                    .append("        <tr><th>User ID</th><td>").append(request.getUserId()).append("</td></tr>\n")
                    .append("        <tr><th>Start Point</th><td>").append(request.getStartPoint()).append("</td></tr>\n")
                    .append("        <tr><th>End Point</th><td>").append(request.getEndPoint()).append("</td></tr>\n")
                    .append("        <tr><th>Datetime</th><td>").append(request.getDatetime()).append("</td></tr>\n")
                    .append("        <tr><th>Preference Parameter</th><td>").append(request.getPreferenceParameter()).append("</td></tr>\n")
                    .append("        <tr><th>Status</th><td>").append(request.getStatus()).append("</td></tr>\n")
                    .append("    </table>\n")
                    .append("</div>\n");
        }

        Crossroad crossroad = document.getCrossroad();
        if (crossroad != null) {
            html.append("<div class=\"section\">\n")
                    .append("    <div class=\"section-title\">Crossroad Information</div>\n")
                    .append("    <table>\n")
                    .append("        <tr><th>ID</th><td>").append(crossroad.getId()).append("</td></tr>\n")
                    .append("        <tr><th>City ID</th><td>").append(crossroad.getCityId()).append("</td></tr>\n")
                    .append("        <tr><th>Coordinates</th><td>").append(crossroad.getCoord()).append("</td></tr>\n")
                    .append("        <tr><th>Type</th><td>").append(crossroad.getType()).append("</td></tr>\n")
                    .append("    </table>\n")
                    .append("</div>\n");
        }

        List<RoadSegment> roadSegments = document.getRoadSegments();
        if (roadSegments != null && !roadSegments.isEmpty()) {
            html.append("<div class=\"section\">\n")
                    .append("    <div class=\"section-title\">Road Segments</div>\n")
                    .append("    <table>\n")
                    .append("        <tr>\n")
                    .append("            <th>ID</th>\n")
                    .append("            <th>City ID</th>\n")
                    .append("            <th>Start Coordinates</th>\n")
                    .append("            <th>End Coordinates</th>\n")
                    .append("            <th>Length</th>\n")
                    .append("            <th>Quality Factor</th>\n")
                    .append("            <th>User Intensity</th>\n")
                    .append("            <th>Rating (QF * UI)</th>\n")
                    .append("        </tr>\n");

            double totalLength = 0;
            double totalRating = 0;

            for (RoadSegment segment : roadSegments) {
                double rating = segment.getRating();
                totalLength += segment.getLength();
                totalRating += rating;

                html.append("        <tr>\n")
                        .append("            <td>").append(segment.getId()).append("</td>\n")
                        .append("            <td>").append(segment.getCityId()).append("</td>\n")
                        .append("            <td>").append(segment.getStartCoord()).append("</td>\n")
                        .append("            <td>").append(segment.getEndCoord()).append("</td>\n")
                        .append("            <td>").append(String.format("%.2f", segment.getLength())).append("</td>\n")
                        .append("            <td>").append(segment.getQualityFactor()).append("</td>\n")
                        .append("            <td>").append(segment.getUserIntensity()).append("</td>\n")
                        .append("            <td>").append(String.format("%.2f", rating)).append("</td>\n")
                        .append("        </tr>\n");
            }

            double avgRating = totalRating / roadSegments.size();

            html.append("        <tr class=\"footer\">\n")
                    .append("            <td colspan=\"4\">TOTAL</td>\n")
                    .append("            <td>").append(String.format("%.2f", totalLength)).append("</td>\n")
                    .append("            <td colspan=\"2\">AVERAGE RATING</td>\n")
                    .append("            <td>").append(String.format("%.2f", avgRating)).append("</td>\n")
                    .append("        </tr>\n")
                    .append("    </table>\n")
                    .append("</div>\n");
        }

        html.append("</body>\n")
                .append("</html>");

        return html.toString();
    }
}
