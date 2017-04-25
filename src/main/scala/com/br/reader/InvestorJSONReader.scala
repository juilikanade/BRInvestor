package com.br.reader
import com.br.model.InvestorOrder
import scala.io.Source
import org.joda.time.DateTime;
import com.br.util.DateUtil
import org.json4s._
import org.json4s.native.JsonMethods._
import scala.collection.mutable.ListBuffer
/**
 * Implementation of [[InvestorReader]] responsible for reading investor from a json file.
 *
 * @param fileName The name of the CSV file to be read.
 * @author jkanade
 */
class InvestorJSONReader(fileName: String) extends InvestorReader {
  implicit val formats = DefaultFormats

  override def readInvestorOrders(): Seq[InvestorOrder] = {
    var inputStream = getClass.getResourceAsStream(fileName);
    var lines = scala.io.Source.fromInputStream(inputStream).getLines
    var jsonString: String = "["
    for (line <- lines) jsonString += line
    jsonString += "]"
    val JSON = parse(jsonString)
    var investorOrders = new ListBuffer[InvestorOrder]()
    for (line <- JSON.children) {
      val id = (line \ "investor_id").extract[Long]
      val date = (line \ "date").extract[String]
      val invType = (line \ "type").extract[String]
      val ticker = (line \ "ticker").extract[String]
      val amount = (line \ "amount").extract[Long]

      investorOrders += InvestorOrder(id, invType, DateUtil.toDateTimeForDashFormat(date), ticker, amount)
    }

    investorOrders.toList
  }

}


object InvestorJSONReader {
  def apply(fileName:String)={
    new InvestorJSONReader(fileName)
  }
}