package com.example.market.util

internal class Constant {

    object API {
        const val HOST = "https://raw.githubusercontent.com/Arm63/armen63.io/master"
        const val FRUIT_LIST = HOST + "/fruit_list/fruits.json"
        const val FRUIT_ITEM = HOST + "/fruit_list/fruits/" // id
        const val FRUIT_ITEM_POSTFIX = "/details.json"
        const val FRUIT_ITEM_STATIC_IMAGE =
            "https://s3-eu-west-1.amazonaws.com/developer-application-test/images/3.jpg"
    }

    object Argument {
        const val ARGUMENT_DATA = "ARGUMENT_DATA"
        const val ARGUMENT_FRUIT = "ARGUMENT_FRUIT"
    }

    object Extra {
        const val FRUIT_ID = "FRUIT_ID"
        const val URL = "URL"
        const val POST_ENTITY = "POST_ENTITY"
        const val REQUEST_TYPE = "REQUEST_TYPE"
        const val NOTIFICATION_DATA = "NOTIFICATION_DATA"
        const val FRUIT = "FRUIT"
        const val MENU_STATE = "MENU_STATE"
    }

    object Symbol {
        const val ASTERISK = "*"
        const val NEW_LINE = "\n"
        const val SPACE = " "
        const val NULL = ""
        const val COLON = ":"
        const val COMMA = ","
        const val SLASH = "/"
        const val DOT = "."
        const val UNDERLINE = "_"
        const val DASH = "-"
        const val AT = "@"
        const val AMPERSAND = "&"
    }

    object Util {
        const val UTF_8 = "UTF-8"
    }

    object RequestType {
        const val FRUIT_LIST = 1
        const val FRUIT_ITEM = 2
    }

    object RequestMethod {
        const val POST = "POST"
        const val GET = "GET"
        const val PUT = "PUT"
    }
}