package com.focus617.mylib.designpattern

import com.focus617.mylib.designpattern.platform.BaseObject
import com.focus617.mylib.logging.WithLogging

// 被构建的产品
class NewProduct : BaseObject() {
    var attrib1: Int = 0
    var attrib2: Int = 0
    var attrib3: Int = 0
    var attrib4: Int = 0
    var attrib5: Int = 0
    var attrib6: Int = 0
    var attrib7: Int = 0
    var attrib8: Int = 0
    var attrib9: Int = 0

    override fun toString(): String =
        "NewProduct(${attrib1},${attrib2},${attrib3},${attrib4}," +
                "${attrib5},${attrib6},${attrib7},${attrib8},${attrib9})"


    class ProductBuilder() : BaseObject() {
        private val product = NewProduct()

        // 对属性分组，分别进行build
        fun buildAttribGroup1(attr1: Int, attr2: Int, attr3: Int): ProductBuilder {
            with(product) {
                attrib1 = attr1
                attrib2 = attr2
                attrib3 = attr3
            }
            return this
        }

        fun buildAttribGroup2(attr4: Int, attr5: Int, attr6: Int): ProductBuilder {
            with(product) {
                attrib4 = attr4
                attrib5 = attr5
                attrib6 = attr6
            }
            return this
        }

        fun buildAttribGroup3(attr7: Int, attr8: Int, attr9: Int): ProductBuilder {
            with(product) {
                attrib7 = attr7
                attrib8 = attr8
                attrib9 = attr9
            }
            return this
        }

        fun build() = product
    }

}

class ClientBuilderStyle {
    companion object : WithLogging() {
        @JvmStatic
        fun main(vararg args: String) {

            val product = NewProduct.ProductBuilder()
                .buildAttribGroup1(1, 2, 3)
                .buildAttribGroup2(4, 5, 6)
                .buildAttribGroup3(7, 8, 9)
                .build()

            LOG.info("Build Success: ${product.toString()} \n")
        }
    }
}