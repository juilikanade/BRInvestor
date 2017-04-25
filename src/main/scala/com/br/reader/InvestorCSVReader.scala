package com.br.reader

import scala.io.Source
import org.joda.time.DateTime;
import com.br.util.DateUtil
import com.br.model.InvestorOrder
/**
 * Implementation of [[InvestorReader]] responsible for reading investor from a CSV file.
 *
 * @param fileName The name of the CSV file to be read.
 * @author jkanade
 */
class InvestorCSVReader(fileName: String) extends InvestorReader {

  override def readInvestorOrders(): Seq[InvestorOrder] = {
    val stream = getClass.getResourceAsStream(fileName)
    for {
      line <- scala.io.Source.fromInputStream(stream).getLines.toVector
      values = line.split(",").map(_.trim)
    } yield InvestorOrder(values(0).toLong, values(3), DateUtil.toDateTime(values(2)), values(1), values(4).toLong)
  }

}

object InvestorCSVReader {
  def apply(fileName:String)={
    new InvestorCSVReader(fileName)
  }
}