package com.example.market.db.entity

class DataSource {

    companion object {
        fun createDataSet(): ArrayList<Fruit> {
            val list = ArrayList<Fruit>()
            list.add(
                Fruit(
                    1,
                    "apple",
                    150,
                    "xndzor ban",
                    "https://raw.githubusercontent.com/Arm63/armen63.io/master/fruit_list/fruits/1/apple.png"
                )
            )
            list.add(
                Fruit(
                    2,
                    "banana",
                    450,
                    "banan ban man",
                    "https://raw.githubusercontent.com/Arm63/armen63.io/master/fruit_list/fruits/2/banana.png"
                )
            )

            list.add(
                Fruit(
                    3,
                    "cherry",
                    230,
                    "vishnya ban",
                    "https://raw.githubusercontent.com/Arm63/armen63.io/master/fruit_list/fruits/3/cherry.png"
                )
            )
            list.add(
                Fruit(
                    4,
                    "orange",
                    400,
                    "apelsin apo",
                    "https://raw.githubusercontent.com/Arm63/armen63.io/master/fruit_list/fruits/4/orange.png"
                )
            )
            list.add(
                Fruit(
                    5,
                    "peach",
                    370,
                    "dexci dvijeni ban",
                    "https://raw.githubusercontent.com/Arm63/armen63.io/master/fruit_list/fruits/5/peach.png"
                )
            )
            list.add(
                Fruit(
                    6,
                    "pineapple",
                    760,
                    "ananas mananas ananas",
                    "https://raw.githubusercontent.com/Arm63/armen63.io/master/fruit_list/fruits/6/pineapple.png"
                )
            )
            list.add(
                Fruit(
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