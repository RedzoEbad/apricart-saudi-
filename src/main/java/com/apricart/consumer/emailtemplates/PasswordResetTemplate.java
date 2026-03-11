package com.apricart.consumer.emailtemplates;

public class PasswordResetTemplate implements EmailTemplate {
    public String message = "";

    @Override
    public String getMessage() {
        return message;
    }

    public PasswordResetTemplate(String user, String imageURL) {
        String companyName = "Apricart Saudi";
        String companyAddress = "Riyadh, Saudi Arab";
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
                "    </style>\n" +
                "    <!--user entered Head Start-->\n" +
                "    <link href='https://fonts.googleapis.com/css?family=Inter' rel='stylesheet'>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: 'Inter';\n" +
                "        }\n" +
                "    </style><!--End Head user entered-->\n" +
                "</head>\n" +
                "<body>\n" +
                "<center class=\"wrapper\" data-link-color=\"#19226D\" data-body-style=\"font-size:14px; font-family:inherit; color:#000000; background-color:#f0f0f0;\">\n" +
                "    <div class=\"webkit\">\n" +
                "        <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\" class=\"wrapper\" bgcolor=\"#f0f0f0\">\n" +
                "            <tbody>\n" +
                "            <tr>\n" +
                "                <td valign=\"top\" bgcolor=\"#f0f0f0\" width=\"100%\">\n" +
                "                    <table width=\"100%\" role=\"content-container\" class=\"outer\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "                        <tbody>\n" +
                "                        <tr>\n" +
                "                            <td width=\"100%\">\n" +
                "                                <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "                                    <tbody>\n" +
                "                                    <tr>\n" +
                "                                        <td>\n" +
                "                                            <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:100%; max-width:600px;\" align=\"center\">\n" +
                "                                                <tbody>\n" +
                "                                                <tr>\n" +
                "                                                    <td role=\"modules-container\" style=\"padding:0px 0px 0px 0px; color:#000000; text-align:left;\" bgcolor=\"#ffffff\" width=\"100%\" align=\"left\">\n" +
                "                                                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"100%\" role=\"module\" data-type=\"columns\" style=\"padding:30px 20px 0px 30px;\">\n" +
                "                                                            <tbody>\n" +
                "                                                            <tr role=\"module-content\">\n" +
                "                                                                <td height=\"100%\" valign=\"top\">\n" +
                "                                                                    <table class=\"column\" width=\"550\" style=\"width:550px; border-spacing:0; border-collapse:collapse; margin:0px 0px 0px 0px;\" cellpadding=\"0\" cellspacing=\"0\" align=\"left\" border=\"0\" bgcolor=\"\">\n" +
                "                                                                        <tbody>\n" +
                "                                                                        <tr>\n" +
                "                                                                            <td style=\"padding:0px;margin:0px;border-spacing:0;\">\n" +
                "                                                                                <table class=\"wrapper\" role=\"module\" data-type=\"image\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout: fixed;\" data-muid=\"b422590c-5d79-4675-8370-a10c2c76af02\">\n" +
                "                                                                                    <tbody>\n" +
                "                                                                                    <tr>\n" +
                "                                                                                        <td style=\"text-align:center;\">\n" +
                "                                                                                                                            <img class=\"max-width\" border=\"0\" style=\"width: 150px;\" alt=\"\" data-proportionally-constrained=\"true\" data-responsive=\"false\" src=\""+imageURL+"\">\n" +
                "                                                                                        </td>\n" +
                "\n" +
                "                                                                                    </tr>\n" +
                "                                                                                    </tbody>\n" +
                "                                                                                </table>\n" +
                "                                                                                <hr style=\"border: 1px solid #FFD54B; margin-top: 12px;\" />\n" +
                "                                                                                <table class=\"module\" role=\"module\" data-type=\"text\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout: fixed;\" data-muid=\"1995753e-0c64-4075-b4ad-321980b82dfe\">\n" +
                "                                                                                    <tbody>\n" +
                "                                                                                    <tr>\n" +
                "                                                                                        <td style=\"padding:8px 0px 0px 0px; line-height:36px; text-align:inherit; color: #19226D\" height=\"100%\" valign=\"top\" bgcolor=\"\" role=\"module-content\"><div><div style=\"font-family: inherit; text-align: inherit;padding-top: 15px;\"><span style=\"color: #19226D; font-size: 20px; font-family: inherit\">Hi "+user+",</span></div><div></div></div></td>\n" +
                "                                                                                    </tr>\n" +
                "                                                                                    </tbody>\n" +
                "                                                                                </table>\n" +
                "                                                                                <table class=\"module\" role=\"module\" data-type=\"text\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout: fixed;\" data-muid=\"2ffRS984-f644-4c25-9a1e-ef76ac62a549\">\n" +
                "                                                                                    <tbody>\n" +
                "                                                                                    <tr>\n" +
                "                                                                                        <td style=\"padding: 12px 18px 4px 0px; line-height: 24px; text-align: inherit;\" height=\"100%\" valign=\"top\" bgcolor=\"\" role=\"module-content\">\n" +
                "                                                                                            <div>\n" +
                "                                                                                                <div style=\"font-family: inherit; text-align: inherit\"><span style=\"font-size: 16px\">Great news! Your password has been successfully reset. You can now log in using your new password.</span></div><div></div>\n" +
                "                                                                                            </div>\n" +
                "                                                                                        </td>\n" +
                "                                                                                    </tr>\n" +
                "                                                                                    <tr>\n" +
                "                                                                                        <td style=\" padding: 12px 18px 4px 0px; line-height: 24px; text-align: inherit;\" height=\"100%\" valign=\"top\" bgcolor=\"\" role=\"module-content\">\n" +
                "                                                                                            <div>\n" +
                "                                                                                                <div style=\"font-family: inherit; text-align: inherit\"><span style=\"font-size: 16px\">\n" +
                "    If you did not request this change, please contact our support team immediately at\n" +
                "    hello@apricart.ai.\n" +
                "</span></div><div></div>\n" +
                "                                                                                            </div>\n" +
                "                                                                                        </td>\n" +
                "                                                                                    </tr>\n" +
                "                                                                                    <tr>\n" +
                "                                                                                        <td style=\"padding: 12px 18px 4px 0px; line-height: 24px; text-align: inherit;\" height=\"100%\" valign=\"top\" bgcolor=\"\" role=\"module-content\">\n" +
                "                                                                                            <div>\n" +
                "                                                                                                <div style=\"font-family: inherit; text-align: inherit\"><span style=\"font-size:16px\">Thank you for ensuring your account's security.</span></div><div></div>\n" +
                "                                                                                            </div>\n" +
                "                                                                                        </td>\n" +
                "                                                                                    </tr>\n" +
                "                                                                                    </tbody>\n" +
                "                                                                                </table>\n" +
                "                                                                                <hr style=\"border: 1px solid #FFD54B; margin-top: 22px; margin-bottom: 22px;\" />\n" +
                "                                                                            </td>\n" +
                "                                                                        </tr>\n" +
                "                                                                        <tr>\n" +
                "                                                                            <td>\n" +
                "                                                                                <p style=\"background-color: #fff; padding: 0px 14px 26px 0px; margin: 0; text-align: left; color: #6d6d6d;\">\n" +
                "                                                                                    Best wishes,\n" +
                "                                                                                    <br>\n" +
                "                                                                                    <span style=\"color: #19226D;\">"+companyName+"</span>\n" +
                "                                                                                </p>\n" +
                "                                                                            </td>\n" +
                "                                                                        </tr>\n" +
                "                                                                        </tbody>\n" +
                "                                                                    </table>\n" +
                "\n" +
                "                                                                </td>\n" +
                "                                                            </tr>\n" +
                "                                                            </tbody>\n" +
                "                                                        </table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "                                                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"module\" data-role=\"module-button\" data-type=\"button\" role=\"module\" style=\"table-layout:fixed;\" width=\"100%\" data-muid=\"e5cea269-a730-4c6b-8691-73d2709adc62\">\n" +
                "                                                            <tbody>\n" +
                "                                                            <tr>\n" +
                "                                                                <td align=\"center\" class=\"outer-td\" style=\"padding:10px 0px 10px 0px;background: #19226D;color: #f1f1f1;\">\n" +
                "                                                                    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"wrapper-mobile\" style=\"text-align:center;\">\n" +
                "                                                                        <tbody>\n" +
                "                                                                        <tr>\n" +
                "                                                                            <td align=\"center\" class=\"inner-td\" style=\"\" target=\"_blank\"><a style=\"color:#ffffff;\" href=\"\">Click Here!</a></td>\n" +
                "                                                                        </tr>\n" +
                "                                                                        </tbody>\n" +
                "                                                                    </table>\n" +
                "                                                                </td>\n" +
                "                                                            </tr>\n" +
                "                                                            </tbody>\n" +
                "                                                        </table>\n" +
                "                                                    </td>\n" +
                "                                                </tr>\n" +
                "                                                </tbody>\n" +
                "                                            </table>\n" +
                "\n" +
                "                                        </td>\n" +
                "                                    </tr>\n" +
                "                                    </tbody>\n" +
                "                                </table>\n" +
                "                            </td>\n" +
                "                        </tr>\n" +
                "                        </tbody>\n" +
                "                    </table>\n" +
                "                </td>\n" +
                "            </tr>\n" +
                "            </tbody>\n" +
                "        </table>\n" +
                "    </div>\n" +
                "</center>\n" +
                "</body>\n" +
                "</html>";
    }

}
