package com.br.stats
import com.br.model.InvestorOrder
import com.br.model.Ticker
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.DateTimeComparator

/**
 * The class which provides all the utility methods to calculate the metrics
 */
class InvestorMetrics() {

  /**
   * Method to fetch the largest trade
   */
  def getLargestTradeForInvestor(investorData: Seq[InvestorOrder], id: Long): InvestorOrder = {
    var investorOrder: InvestorOrder = investorData.filter(_.investorId == id).maxBy(_.amount)
    investorOrder
  }

  /**
   * Method to fetch the smallest trade
   */
  def getSmallestTradeForInvestor(investorData: Seq[InvestorOrder], id: Long): InvestorOrder = {
    var investorOrder = investorData.filter(_.investorId == id).minBy(_.amount)
    investorOrder
  }

  /**
   * Method to fetch the active investors
   */
  def getActiveInvestors(investorData: Seq[InvestorOrder]): Seq[Long] = {
//TODO Logic can be optimized further
    var listActiveInvestors = scala.collection.mutable.Set[Long]()
    var investorToDateMappedOrders = getInvestorToDateMappedOrders(investorData)

    investorToDateMappedOrders.keys.foreach(investor => {

      var dateToOrderMap = investorToDateMappedOrders(investor)

      if (dateToOrderMap.keys.toList.size > 1) { //iterating only if the orders are on different days
        var listOfNeighborDates = dateToOrderMap.keys.toList.sortWith(_.isBefore(_)).sliding(2) //sorting the dates

        var totalNumberOfOrders = 0

        while (listOfNeighborDates.hasNext) // this can be put in breakable  
        {

          var sublist = listOfNeighborDates.next
          var investorDate1 = sublist(0)
          var investorDate2 = sublist(1)

          var noOfDays = Days.daysBetween(investorDate1, investorDate2).getDays()
          if (noOfDays == 1) {
            totalNumberOfOrders = totalNumberOfOrders + dateToOrderMap(investorDate1).size + dateToOrderMap(investorDate2).size
            if (totalNumberOfOrders > 2)
              listActiveInvestors += investor
          }

        }

      }
    })
    listActiveInvestors.toList
  }

  /**
   * Method to fetch the buy and hold investors
   */
  def getBuyAndHoldInvestors(investorData: Seq[InvestorOrder]): Seq[Long] = {
    var listBuyAndHoldInvestors = scala.collection.mutable.ListBuffer.empty[Long]

    var mapInvestorToOrders = investorData.groupBy(_.investorId)

    mapInvestorToOrders.keys.foreach(investor => {
      var buys = mapInvestorToOrders(investor).filter(_.invType.equalsIgnoreCase("BUY")).size
      var sells = mapInvestorToOrders(investor).filter(_.invType.equalsIgnoreCase("SELL")).size
      if (buys > sells)
        listBuyAndHoldInvestors += investor
    })

    listBuyAndHoldInvestors.sorted
  }

  /**
   * Method to fetch the most bought ticker
   */
  def getMostBoughtTicker(investorOrderData: Seq[InvestorOrder]): Ticker = {
    var tickerList = getTickerList(investorOrderData)
    tickerList.maxBy(_.buys)
  }

  /**
   * Method to fetch most sold Ticker
   */
  def getMostSoldTicker(investorOrderData: Seq[InvestorOrder]): Ticker = {
    var tickerList = getTickerList(investorOrderData)
    tickerList.maxBy(_.sells)
  }

  /**
   * Method to fetch the parent ticker list for all the investors
   */
  def getTickerList(investorOrderData: Seq[InvestorOrder]) = {
    var mapTickerToOrders = investorOrderData.groupBy(_.ticker)
    var tickerList = scala.collection.mutable.ListBuffer.empty[Ticker]

    mapTickerToOrders.keys.foreach(ticker => {
      var buys = mapTickerToOrders(ticker).filter(_.invType.equalsIgnoreCase("BUY")).size
      var sells = mapTickerToOrders(ticker).filter(_.invType.equalsIgnoreCase("SELL")).size
      tickerList += Ticker(ticker, buys, sells)

    })
    tickerList
  }
/**
 * Method which will return a map of investor against a date map ( map which has date to list of orders ).Required to find the active list of users
 */
  def getInvestorToDateMappedOrders(investorData: Seq[InvestorOrder]): scala.collection.mutable.HashMap[Long, Map[DateTime, Seq[InvestorOrder]]] = {
    var mapInvestors = investorData.groupBy(_.investorId)
    var mapInvestorToDateAndOrders = scala.collection.mutable.HashMap.empty[Long, Map[DateTime, Seq[InvestorOrder]]]

    mapInvestors.keys.foreach(investor => {
      //fetch list for each investor,sort and compare consecutive elements
      var listOfOrdersForInvestor = mapInvestors(investor)
      var mapDateToOrdersForInvestors = listOfOrdersForInvestor.groupBy(_.date)
      mapInvestorToDateAndOrders += (investor -> mapDateToOrdersForInvestors) //investor -> date|List[InvestorOrder]
    })
    mapInvestorToDateAndOrders
  }
}

object InvestorMetrics {

  def apply() = new InvestorMetrics()

}