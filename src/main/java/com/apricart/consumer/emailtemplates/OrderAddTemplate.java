package com.apricart.consumer.emailtemplates;

import com.apricart.consumer.enity.OrderItem;
import com.apricart.consumer.enity.Orders;

import java.io.IOException;
import java.time.format.DateTimeFormatter;


public class OrderAddTemplate implements EmailTemplate {
    public String message = "";

    @Override
    public String getMessage() {
        return message;
    }

    public OrderAddTemplate(Orders order, String imageURL) throws IOException {
        String companyName = "Apricart Saudi";
        String companyAddress = "Riyadh, Saudi Arab";

        String orderNumber = order.getId();
        String user = order.getCustomer().getName();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = order.getCreateDateTime().format(formatter);

        StringBuilder itemsHtml = new StringBuilder();
        for (OrderItem item : order.getOrderItems()) {
            itemsHtml.append("<tr class=\"row-border-bottom\">")
                    .append("<th class=\"table-stack product-image-wrapper stack-column-center\" width=\"1\" style=\"mso-line-height-rule: exactly; border-bottom-width: 2px; border-bottom-color: #dadada; border-bottom-style: solid; padding: 0px 13px 0px 0;\" bgcolor=\"#ffffff\" valign=\"middle\">")
                    .append("<img width=\"140\" class=\"product-image\" src=\"").append(item.getProductWarehouse().getProduct().getImage()).append("\" alt=\"Product Image\" style=\"vertical-align: middle; text-align: center; width: 140px; max-width: 140px; height: auto !important; border-radius: 1px; padding: 0px;\">")
                    .append("</th>")
                    .append("<th class=\"product-details-wrapper table-stack stack-column\" style=\"mso-line-height-rule: exactly; padding-top: 13px; padding-bottom: 13px; border-bottom-width: 2px; border-bottom-color: #dadada; border-bottom-style: solid;\" bgcolor=\"#ffffff\" valign=\"middle\">")
                    .append("<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\"min-width: 100%;\" role=\"presentation\">")
                    .append("<tbody>")
                    .append("<tr>")
                    .append("<th class=\"line-item-description\" style=\"mso-line-height-rule: exactly; font-family: -apple-system,BlinkMacSystemFont,'Segoe UI',Arial,'Karla'; font-size: 16px; line-height: 26px; font-weight: 400; color: #666363; padding: 13px 6px 13px 0;\" align=\"left\" bgcolor=\"#ffffff\" valign=\"top\">")
                    .append("<p style=\"mso-line-height-rule: exactly; font-family: -apple-system,BlinkMacSystemFont,'Segoe UI',Arial,'Karla'; font-size: 16px; line-height: 26px; font-weight: 400; color: #666363; margin: 0;\" align=\"left\">")
                    .append("<a href=\"#\" target=\"_blank\" style=\"color: #666363; text-decoration: none !important; text-underline: none; word-wrap: break-word; text-align: left !important; font-weight: bold;\">")
                    .append(item.getTitle())
                    .append("</a>")
                    .append("</p>")
                    .append("</th>")
                    .append("<th style=\"mso-line-height-rule: exactly;\" bgcolor=\"#ffffff\" valign=\"top\"></th>")
                    .append("<th class=\"right line-item-qty\" width=\"1\" style=\"mso-line-height-rule: exactly; white-space: nowrap; padding: 13px 0 13px 13px;\" align=\"right\" bgcolor=\"#ffffff\" valign=\"top\">")
                    .append("<p style=\"mso-line-height-rule: exactly; font-family: -apple-system,BlinkMacSystemFont,'Segoe UI',Arial,'Karla'; font-size: 16px; line-height: 26px; font-weight: 400; color: #666363; margin: 0;\" align=\"right\">")
                    .append("×&nbsp;").append(item.getQuantity())
                    .append("</p>")
                    .append("</th>")
                    .append("<th class=\"right line-item-line-price\" width=\"1\" style=\"mso-line-height-rule: exactly; white-space: nowrap; padding: 13px 0 13px 26px;\" align=\"right\" bgcolor=\"#ffffff\" valign=\"top\">")
                    .append("<p style=\"mso-line-height-rule: exactly; font-family: -apple-system,BlinkMacSystemFont,'Segoe UI',Arial,'Karla'; font-size: 16px; line-height: 26px; font-weight: 400; color: #666363; margin: 0;\" align=\"right\">")
                    .append(item.getTotalAmount())
                    .append("</p>")
                    .append("</th>")
                    .append("</tr>")
                    .append("</tbody>")
                    .append("</table>")
                    .append("</th>")
                    .append("</tr>");
        }

String msg =
        this.message = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1\">\n" +
                "\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=Edge\">\n" +
                "\n" +
                "    <style type=\"text/css\">\n" +
                "        body, p, div {\n" +
                "            font-family: inherit;\n" +
                "            font-size: 14px;\n" +
                "        }\n" +
                "\n" +
                "        body {\n" +
                "            color: #000000;\n" +
                "        }\n" +
                "\n" +
                "            body a {\n" +
                "                color: #19226D;\n" +
                "                text-decoration: none;\n" +
                "            }\n" +
                "\n" +
                "        p {\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "\n" +
                "        table.wrapper {\n" +
                "            width: 100% !important;\n" +
                "            table-layout: fixed;\n" +
                "            -webkit-font-smoothing: antialiased;\n" +
                "            -webkit-text-size-adjust: 100%;\n" +
                "            -moz-text-size-adjust: 100%;\n" +
                "            -ms-text-size-adjust: 100%;\n" +
                "        }\n" +
                "\n" +
                "        img.max-width {\n" +
                "            max-width: 100% !important;\n" +
                "        }\n" +
                "\n" +
                "        .column.of-2 {\n" +
                "            width: 50%;\n" +
                "        }\n" +
                "\n" +
                "        .column.of-3 {\n" +
                "            width: 33.333%;\n" +
                "        }\n" +
                "\n" +
                "        .column.of-4 {\n" +
                "            width: 25%;\n" +
                "        }\n" +
                "\n" +
                "        @media screen and (max-width:480px) {\n" +
                "            .preheader .rightColumnContent,\n" +
                "            .footer .rightColumnContent {\n" +
                "                text-align: left !important;\n" +
                "            }\n" +
                "\n" +
                "                .preheader .rightColumnContent div,\n" +
                "                .preheader .rightColumnContent span,\n" +
                "                .footer .rightColumnContent div,\n" +
                "                .footer .rightColumnContent span {\n" +
                "                    text-align: left !important;\n" +
                "                }\n" +
                "\n" +
                "            .preheader .rightColumnContent,\n" +
                "            .preheader .leftColumnContent {\n" +
                "                font-size: 80% !important;\n" +
                "                padding: 5px 0;\n" +
                "            }\n" +
                "\n" +
                "            table.wrapper-mobile {\n" +
                "                width: 100% !important;\n" +
                "                table-layout: fixed;\n" +
                "            }\n" +
                "\n" +
                "            img.max-width {\n" +
                "                height: auto !important;\n" +
                "                max-width: 100% !important;\n" +
                "            }\n" +
                "\n" +
                "            a.bulletproof-button {\n" +
                "                display: block !important;\n" +
                "                width: auto !important;\n" +
                "                font-size: 80%;\n" +
                "                padding-left: 0 !important;\n" +
                "                padding-right: 0 !important;\n" +
                "            }\n" +
                "\n" +
                "            .columns {\n" +
                "                width: 100% !important;\n" +
                "            }\n" +
                "\n" +
                "            .column {\n" +
                "                display: block !important;\n" +
                "                width: 100% !important;\n" +
                "                padding-left: 0 !important;\n" +
                "                padding-right: 0 !important;\n" +
                "                margin-left: 0 !important;\n" +
                "                margin-right: 0 !important;\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        .borderstyle {\n" +
                "            border: 1px solid #dddddd;\n" +
                "        }\n" +
                "    </style>\n" +
                "    <!--user entered Head Start-->\n" +
                "    \n" +
                "    <link href='https://fonts.googleapis.com/css?family=Inter' rel='stylesheet'>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: 'Inter';\n" +
                "        }\n" +
                "    </style><!--End Head user entered-->\n" +
               "</head>\n" +
               "<body>\n" +
               "    <center class=\"wrapper\" data-link-color=\"#19226D\" data-body-style=\"font-size:14px; font-family:inherit; color:#000000; background-color:#f0f0f0;\">\n" +
               "        <div class=\"webkit\">\n" +
               "            <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\" class=\"wrapper\" bgcolor=\"#f0f0f0\">\n" +
               "                <tbody>\n" +
               "                    <tr>\n" +
               "                        <td valign=\"top\" bgcolor=\"#f0f0f0\" width=\"100%\">\n" +
               "                            <table width=\"100%\" role=\"content-container\" class=\"outer\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
               "                                <tbody>\n" +
               "                                    <tr>\n" +
               "                                        <td width=\"100%\">\n" +
               "                                            <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
               "                                                <tbody>\n" +
               "                                                    <tr>\n" +
               "                                                        <td>\n" +
               "                                                            <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:100%; max-width:600px;\" align=\"center\">\n" +
               "                                                                <tbody>\n" +
               "                                                                    <tr>\n" +
               "                                                                        <td role=\"modules-container\" style=\"padding:0px 0px 0px 0px; color:#000000; text-align:left;\" bgcolor=\"#ffffff\" width=\"100%\" align=\"left\">\n" +
               "                                                                            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"100%\" role=\"module\" data-type=\"columns\" style=\"padding:30px 20px 0px 30px;\">\n" +
               "                                                                                <tbody>\n" +
               "                                                                                    <tr role=\"module-content\">\n" +
               "                                                                                        <td height=\"100%\" valign=\"top\">\n" +
               "                                                                                            <table class=\"column\" width=\"550\" style=\"width:550px; border-spacing:0; border-collapse:collapse; margin:0px 0px 0px 0px;\" cellpadding=\"0\" cellspacing=\"0\" align=\"left\" border=\"0\" bgcolor=\"\">\n" +
               "                                                                                                <tbody>\n" +
               "                                                                                                    <tr>\n" +
               "                                                                                                        <td style=\"padding:0px;margin:0px;border-spacing:0;\">\n" +
               "                                                                                                            <table class=\"wrapper\" role=\"module\" data-type=\"image\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout: fixed;\" data-muid=\"b422590c-5d79-4675-8370-a10c2c76af02\">\n" +
               "                                                                                                                <tbody>\n" +
               "                                                                                                                    <tr>\n" +
               "                                                                                                                        <td style=\"text-align:center;\">\n" +
                "                                                                                                                            <img class=\"max-width\" border=\"0\" style=\"width: 150px;\" alt=\"\" data-proportionally-constrained=\"true\" data-responsive=\"false\" src=\""+imageURL+"\">\n" +
               "                                                                                                                        </td>\n" +
               "\n" +
               "                                                                                                                    </tr>\n" +
               "                                                                                                                </tbody>\n" +
               "                                                                                                            </table>\n" +
               "                                                                                                            <hr style=\"border: 1px solid #FFD54B; margin-top: 12px;\" />\n" +
               "                                                                                                            <table class=\"module\" role=\"module\" data-type=\"text\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout: fixed;\" data-muid=\"1995753e-0c64-4075-b4ad-321980b82dfe\">\n" +
               "                                                                                                                <tbody>\n" +
               "                                                                                                                    <tr>\n" +
               "                                                                                                                        <td style=\"padding:8px 0px 0px 0px; line-height:36px; text-align:inherit; color: #19226D\" height=\"100%\" valign=\"top\" bgcolor=\"\" role=\"module-content\"><div><div style=\"font-family: inherit; text-align: inherit;padding-top: 15px;\"><span style=\"color: #19226D; font-size: 20px; font-family: inherit\">Hi "+user+",</span></div><div></div></div></td>\n" +
               "                                                                                                                    </tr>\n" +
               "                                                                                                                </tbody>\n" +
               "                                                                                                            </table>\n" +
               "                                                                                                            <table class=\"module\" role=\"module\" data-type=\"text\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout: fixed;\" data-muid=\"2ffRS984-f644-4c25-9a1e-ef76ac62a549\">\n" +
               "                                                                                                                <tbody>\n" +
               "                                                                                                                    <tr>\n" +
               "                                                                                                                        <td style=\"padding: 12px 18px 4px 0px; line-height: 24px; text-align: inherit;\" height=\"100%\" valign=\"top\" bgcolor=\"\" role=\"module-content\">\n" +
               "                                                                                                                            <div>\n" +
               "                                                                                                                                <div style=\"font-family: inherit; text-align: inherit\"><span style=\"font-size: 16px\">Thank you for shopping with us! We're thrilled to confirm your order #"+orderNumber+". Here's what you can look forward to:</span></div><div></div>\n" +
               "                                                                                                                            </div>\n" +
               "                                                                                                                        </td>\n" +
               "                                                                                                                    </tr>\n" +
               "\n" +
               "\n" +
               "                                                                                                                </tbody>\n" +
               "                                                                                                            </table>\n" +
               "\n" +
               "                                                                                                            <table class=\"table-inner\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\"min-width: 100%;\" role=\"presentation\">\n" +
               "                                                                                                                <tbody>\n" +
               "                                                                                                                    <tr>\n" +
               "                                                                                                                        <th class=\"product-table\" style=\"mso-line-height-rule: exactly;\" bgcolor=\"#ffffff\" valign=\"top\">\n" +
               "                                                                                                                            <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\"min-width: 100%;\" role=\"presentation\">\n" +
               "                                                                                                                                <tbody>\n" +
               "                                                                                                                                    <tr>\n" +
               "\n" +
               "                                                                                                                                        <th colspan=\"2\" class=\"product-table-h3-wrapper\" style=\"mso-line-height-rule: exactly;\" bgcolor=\"#ffffff\" valign=\"top\">\n" +
               "                                                                                                                                            <h3 data-key=\"1468271_item\" style=\" color: #bdbdbd; font-size: 16px; line-height: 14px; font-weight: 500;   letter-spacing: 1px; margin: 0;\" align=\"left\">Order Details:</h3>\n" +
               "                                                                                                                                            <h3 data-key=\"1468271_item\" style=\"color: #bdbdbd; font-size: 16px; line-height: 52px; font-weight: 700; text-transform: uppercase; border-bottom-width: 2px; border-bottom-color: #dadada; border-bottom-style: solid; letter-spacing: 1px; margin: 0;\" align=\"left\">"+formattedDate+"</h3>\n" +
               "                                                                                                                                        </th>\n" +
               "                                                                                                                                    </tr>\n" +
               "\n" +
               "                                                                                                                                    <!-- Bold 2 -->\n" +
               "                                                                                                                                    <!-- end Bold 2 -->\n" + itemsHtml+
               "\n" +
               "                                                                                                                                    <tr>\n" +
               "                                                                                                                                        <th colspan=\"2\" class=\"product-empty-row\" style=\"mso-line-height-rule: exactly;\" bgcolor=\"#ffffff\" valign=\"top\"></th>\n" +
               "                                                                                                                                    </tr>\n" +
               "\n" +
               "                                                                                                                                    <!-- Bold 2 -->\n" +
               "                                                                                                                                    <!-- end Bold 2 -->\n" +

               "                                                                                                                                </tbody>\n" +
               "                                                                                                                            </table>\n" +
               "                                                                                                                        </th>\n" +
               "                                                                                                                    </tr>\n" +
               "\n" +
               "                                                                                                                </tbody>\n" +
               "                                                                                                            </table>\n" +
               "\n" +
               "\n" +
               "\n" +
               "                                                                                                            <table class=\"module\" role=\"module\" data-type=\"text\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout: fixed;\" data-muid=\"2ffRS984-f644-4c25-9a1e-ef76ac62a549\">\n" +
               "                                                                                                                <tbody>\n" +
               "                                                                                                                    <tr>\n" +
               "                                                                                                                        <td style=\"padding: 12px 18px 4px 0px; line-height: 24px; text-align: inherit;\" height=\"100%\" valign=\"top\" bgcolor=\"\" role=\"module-content\">\n" +
               "                                                                                                                            <div>\n" +
               "                                                                                                                                <div style=\"font-family: inherit; text-align: inherit\"><span style=\"font-size: 16px\">Shipping To:</span></div><div>"+order.getCustomerAddress().getAddressDetail()+"</div>\n" +
               "                                                                                                                            </div>\n" +
               "                                                                                                                        </td>\n" +
               "                                                                                                                    </tr>\n" +
               "                                                                                                                    <tr>\n" +
               "                                                                                                                        <td style=\"padding:12px 18px 4px 0px; line-height: 24px; text-align: inherit;\" height=\"100%\" valign=\"top\" bgcolor=\"\" role=\"module-content\">\n" +
               "                                                                                                                            <div>\n" +
               "                                                                                                                                <div style=\"font-family: inherit; text-align: inherit\"><span style=\"font-size: 16px\">Billing To:</span></div><div>"+order.getCustomerAddress().getAddressDetail()+"</div>\n" +
               "                                                                                                                            </div>\n" +
               "                                                                                                                        </td>\n" +
               "                                                                                                                    </tr>\n" +
               "                                                                                                                    <tr>\n" +
               "                                                                                                                        <td class=\"\" style=\" font-size: 16px; line-height: 26px; padding: 6px 0;\" align=\"left\" bgcolor=\"#ffffff\">Total Amount:</td>\n" +
               "                                                                                                                        <td class=\"table-text\" style= \" font-size: 16px; line-height: 26px; font-weight: 400; color: #666363; padding: 6px 0;\" align=\"right\" bgcolor=\"#ffffff\" valign=\"middle\">"+order.getGrandTotal()+"</td>\n" +
               "                                                                                                                    </tr>\n" +
               "                                                                                                                </tbody>\n" +
               "                                                                                                            </table>\n" +
               "\n" +
               "                                                                                                            <!--<table  class=\"module\" role=\"module\" data-type=\"text\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout:  fixed;\" data-muid=\"2ffRS984-f644-4c25-9a1e-ef76ac62a549\">\n" +
               "        <tr>\n" +
               "            <th class=\"pricing-table\" style=\"mso-line-height-rule: exactly; padding: 13px 0;\" bgcolor=\"#ffffff\" valign=\"top\">\n" +
               "                <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\"min-width: 100%;\" role=\"presentation\">\n" +
               "\n" +
               "                    <tbody>\n" +
               "                        <tr>\n" +
               "                            <th class=\"table-title\" style=\"mso-line-height-rule: exactly;  font-size: 16px; line-height: 26px;  color: #666363; width: 65%; padding: 6px 0;\" align=\"left\" bgcolor=\"#ffffff\" valign=\"top\">\n" +
               "                                <span data-key=\"1468271_discount\">Shipping to address:</span><br/>\n" +
               "                                <span data-key=\"1468271_discount\">[Address]</span>\n" +
               "                            </th>\n" +
               "\n" +
               "                        </tr>\n" +
               "\n" +
               "                        <tr>\n" +
               "                            <th class=\"table-title\" data-key=\"1468271_subtotal\" style=\"mso-line-height-rule: exactly;  font-size: 16px; line-height: 26px; font-weight: bold; color: #666363; width: 65%; padding: 6px 0;\" align=\"left\" bgcolor=\"#ffffff\" valign=\"top\">Subtotal</th>\n" +
               "                            <th class=\"table-text\" style=\"mso-line-height-rule: exactly; font-size: 16px; line-height: 26px; font-weight: 400; color: #666363; width: 35%; padding: 6px 0;\" align=\"right\" bgcolor=\"#ffffff\" valign=\"middle\">$89.00</th>\n" +
               "                        </tr>\n" +
               "\n" +
               "                        <tr>\n" +
               "                            <th class=\"table-title\" style=\"mso-line-height-rule: exactly;  font-size: 16px; line-height: 26px; font-weight: bold; color: #666363; width: 65%; padding: 6px 0;\" align=\"left\" bgcolor=\"#ffffff\" valign=\"top\">Royal Mail Tracked &amp; Signed (4-9 days)</th>\n" +
               "                            <th class=\"table-text\" style=\"mso-line-height-rule: exactly; font-size: 16px; line-height: 26px; font-weight: 400; color: #666363; width: 35%; padding: 6px 0;\" align=\"right\" bgcolor=\"#ffffff\" valign=\"middle\">$0.00</th>\n" +
               "                        </tr>\n" +
               "\n" +
               "                        <tr class=\"pricing-table-total-row\">\n" +
               "                            <th class=\"table-title\" data-key=\"1468271_total\" style=\"mso-line-height-rule: exactly;  font-size: 16px; line-height: 26px; font-weight: bold; color: #666363; width: 65%; padding: 6px 0;\" align=\"left\" bgcolor=\"#ffffff\" valign=\"top\">Total</th>\n" +
               "                            <th class=\"table-text\" style=\"mso-line-height-rule: exactly;  font-size: 16px; line-height: 26px; font-weight: 400; color: #666363; width: 35%; padding: 6px 0;\" align=\"right\" bgcolor=\"#ffffff\" valign=\"middle\">$89.00</th>\n" +
               "                        </tr>\n" +
               "\n" +
               "                    </tbody>\n" +
               "                </table>\n" +
               "            </th>\n" +
               "        </tr>\n" +
               "    </table>-->\n" +
               "\n" +
               "\n" +
               "                                                                                                            <table class=\"module\" role=\"module\" data-type=\"text\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout: fixed;\" data-muid=\"2ffRS984-f644-4c25-9a1e-ef76ac62a549\">\n" +
               "                                                                                                                <tbody>\n" +
               "                                                                                                                    <tr>\n" +
               "                                                                                                                        <td style=\"padding: 12px 18px 4px 0px; line-height: 24px; text-align: inherit;\" height=\"100%\" valign=\"top\" bgcolor=\"\" role=\"module-content\">\n" +
               "                                                                                                                            <div>\n" +
               "                                                                                                                                <div style=\"font-family: inherit; text-align: inherit\"><span style=\"font-size: 16px\">Your order is being processed, and we’ll notify you as soon as it ships. Track your order status [here](Link to Order Status Page).</span></div><div></div>\n" +
               "                                                                                                                            </div>\n" +
               "                                                                                                                        </td>\n" +
               "                                                                                                                    </tr>\n" +
               "\n" +
               "                                                                                                                    <tr>\n" +
               "                                                                                                                        <td style=\"padding: 12px 18px 4px 0px; line-height: 24px; text-align: inherit;\" height=\"100%\" valign=\"top\" bgcolor=\"\" role=\"module-content\">\n" +
               "                                                                                                                            <div>\n" +
               "                                                                                                                                <div style=\"font-family: inherit; text-align: inherit\"><span style=\"font-size: 16px\">Got questions? Our friendly customer support team is here to help: hello@apricart.ai.</span></div><div></div>\n" +
               "                                                                                                                            </div>\n" +
               "                                                                                                                        </td>\n" +
               "                                                                                                                    </tr>\n" +
               "                                                                                                                    <tr>\n" +
               "                                                                                                                        <td style=\" padding: 12px 18px 4px 0px; line-height: 24px; text-align: inherit;\" height=\"100%\" valign=\"top\" bgcolor=\"\" role=\"module-content\">\n" +
               "                                                                                                                            <div>\n" +
               "                                                                                                                                <div style=\" font-family: inherit; text-align: inherit\"><span style=\"font-size: 16px\">Thank you for being a valued customer!</span></div><div></div>\n" +
               "                                                                                                                            </div>\n" +
               "                                                                                                                        </td>\n" +
               "                                                                                                                    </tr>\n" +
               "                                                                                                                </tbody>\n" +
               "                                                                                                            </table>\n" +
               "\n" +
               "                                                                                                            <hr style=\"border: 1px solid #FFD54B; margin-top: 22px; margin-bottom: 22px;\" />\n" +
               "                                                                                                        </td>                                                                                                        \n" +
               "                                                                                                    </tr>\n" +
               "                                                                                                    <tr>\n" +
               "                                                                                                        <td>\n" +
               "                                                                                                            <p style=\"background-color: #fff; padding: 0px 14px 26px 0px; margin: 0; text-align: left; color: #6d6d6d;\">\n" +
               "                                                                                                                Best wishes,\n" +
               "                                                                                                                <br>\n" +
               "                                                                                                                <span style=\"color: #19226D;\">"+companyName+"</span>\n" +
               "                                                                                                            </p>\n" +
               "                                                                                                        </td>\n" +
               "                                                                                                    </tr>\n" +
               "                                                                                                </tbody>\n" +
               "                                                                                            </table>\n" +
               "\n" +
               "                                                                                        </td>\n" +
               "                                                                                    </tr>\n" +
               "                                                                                </tbody>\n" +
               "                                                                            </table>\n" +
               "                                                                           \n" +
               "                                                                             \n" +
               "\n" +
               "\n" +
               "                                                                            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"module\" data-role=\"module-button\" data-type=\"button\" role=\"module\" style=\"table-layout:fixed;\" width=\"100%\" data-muid=\"e5cea269-a730-4c6b-8691-73d2709adc62\">\n" +
               "                                                                                <tbody>\n" +
               "                                                                                    <tr>\n" +
               "                                                                                        <td align=\"center\" class=\"outer-td\" style=\"padding:10px 0px 10px 0px;background: #19226D;color: #f1f1f1;\">\n" +
               "                                                                                            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"wrapper-mobile\" style=\"text-align:center;\">\n" +
               "                                                                                                <tbody>\n" +
               "                                                                                                    <tr>\n" +
               "                                                                                                        <td align=\"center\" class=\"inner-td\" style=\"\" target=\"_blank\"><a style=\"color:#ffffff;\" href=\"\">Click Here!</a></td>\n" +
               "                                                                                                    </tr>\n" +
               "                                                                                                </tbody>\n" +
               "                                                                                            </table>\n" +
               "                                                                                        </td>\n" +
               "                                                                                    </tr>\n" +
               "                                                                                </tbody>\n" +
               "                                                                            </table>\n" +
               "                                                                        </td>\n" +
               "                                                                    </tr>\n" +
               "                                                                </tbody>\n" +
               "                                                            </table>\n" +
               "\n" +
               "                                                        </td>\n" +
               "                                                    </tr>\n" +
               "                                                </tbody>\n" +
               "                                            </table>\n" +
               "                                        </td>\n" +
               "                                    </tr>\n" +
               "                                </tbody>\n" +
               "                            </table>\n" +
               "                        </td>\n" +
               "                    </tr>\n" +
               "                </tbody>\n" +
               "            </table>\n" +
               "        </div>\n" +
               "    </center>\n" +
               "</body>\n" +
               "</html>";
    }
}
