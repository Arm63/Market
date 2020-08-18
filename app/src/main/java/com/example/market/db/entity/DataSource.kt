package com.example.market.db.entity

class DataSource {

    companion object {
        fun createDataSet(): ArrayList<BlogPost> {
            val list = ArrayList<BlogPost>()
            list.add(
                BlogPost(
                    1,
                    "apple",
                    150,
                    "xndzor ban",
                    "https://raw.githubusercontent.com/Arm63/armen63.io/master/fruit_list/fruits/1/apple.png"
                )
            )
            list.add(
                BlogPost(
                    2,
                    "banana",
                    450,
                    "banan ban man",
                    "https://raw.githubusercontent.com/Arm63/armen63.io/master/fruit_list/fruits/2/banana.png"
                )
            )

            list.add(
                BlogPost(
                    3,
                    "cherry",
                    230,
                    "vishnya ban",
                    "https://raw.githubusercontent.com/Arm63/armen63.io/master/fruit_list/fruits/3/cherry.png"
                )
            )
            list.add(
                BlogPost(
                    4,
                    "orange",
                    400,
                    "apelsin apo",
                    "https://raw.githubusercontent.com/Arm63/armen63.io/master/fruit_list/fruits/4/orange.png"
                )
            )
            list.add(
                BlogPost(
                    5,
                    "peach",
                    370,
                    "dexci dvijeni ban",
                    "https://raw.githubusercontent.com/Arm63/armen63.io/master/fruit_list/fruits/5/peach.png"
                )
            )
            list.add(
                BlogPost(
                    6,
                    "pineapple",
                    760,
                    "ananas mananas ananas",
                    "https://raw.githubusercontent.com/Arm63/armen63.io/master/fruit_list/fruits/6/pineapple.png"
                )
            )
            list.add(
                BlogPost(
                    7,
                    "strawberry",
                    320,
                    "klubno klubno klubno",
                    "https://raw.githubusercontent.com/Arm63/armen63.io/master/fruit_list/fruits/7/strawberry.png"
                )
            )

            return list
        }
    }
}