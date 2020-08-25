package dto

import java.lang

case class Details(epsilon: lang.Double, discount: lang.Double, stepsize: lang.Double, averagereward: lang.Double,
                   notes: String, numbergames: Int, xgamewins: Int, ogamewins: Int,
                   drawn: Int, id: String, date: String)

