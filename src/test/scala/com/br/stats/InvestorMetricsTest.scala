package com.br.stats

import org.scalatest.Matchers._
import org.scalatest.{BeforeAndAfter, FunSuite}
import com.br.model.InvestorOrder
import com.br.model.Ticker
import com.br.util.DateUtil
/**
  *Tests to check metrics
  * @author jkanade
  */
class InvestorMetricsTest extends FunSuite with BeforeAndAfter {

  var metrics: InvestorMetrics = _
  var investorData = scala.collection.mutable.ListBuffer.empty[InvestorOrder]
  investorData += InvestorOrder(1,"SELL",DateUtil.toDateTimeForDashFormat("1-12-2016"),"BR",1285)
  investorData += InvestorOrder(1,"BUY",DateUtil.toDateTimeForDashFormat("1-13-2016"),"AAPL",223985)
  investorData += InvestorOrder(1,"BUY",DateUtil.toDateTimeForDashFormat("1-14-2016"),"BR",3456)
  investorData += InvestorOrder(2,"BUY",DateUtil.toDateTimeForDashFormat("1-12-2016"),"BR",123985)
  investorData += InvestorOrder(2,"BUY",DateUtil.toDateTimeForDashFormat("1-12-2016"),"BR",985)
  investorData += InvestorOrder(2,"BUY",DateUtil.toDateTimeForDashFormat("1-12-2016"),"BR",3985)
  investorData += InvestorOrder(3,"BUY",DateUtil.toDateTimeForDashFormat("1-11-2016"),"BR",6985)
  investorData += InvestorOrder(3,"BUY",DateUtil.toDateTimeForDashFormat("1-12-2016"),"BR",7985)
  investorData += InvestorOrder(3,"SELL",DateUtil.toDateTimeForDashFormat("1-13-2016"),"MO",13985)
  investorData += InvestorOrder(3,"SELL",DateUtil.toDateTimeForDashFormat("1-14-2016"),"MO",123985)
  investorData += InvestorOrder(3,"SELL",DateUtil.toDateTimeForDashFormat("1-12-2016"),"MO",823985)
  investorData += InvestorOrder(4,"SELL",DateUtil.toDateTimeForDashFormat("1-12-2016"),"MO",985)
  investorData += InvestorOrder(4,"SELL",DateUtil.toDateTimeForDashFormat("1-12-2016"),"MO",3985)
  before {
    metrics = new InvestorMetrics()
  }

  test("get max sold ticker") {
    metrics.getMostSoldTicker(investorData) shouldBe Ticker("MO",0,5)
  }

   test("get max bought ticker") {
    metrics.getMostBoughtTicker(investorData) shouldBe Ticker("BR",6,1)
  }
   
   
   test("get active investors") {
    metrics.getActiveInvestors(investorData) shouldBe Seq(1,3)
  }
   
    test("get largest trade for investor") {
    metrics.getLargestTradeForInvestor(investorData,1) shouldBe InvestorOrder(1,"BUY",DateUtil.toDateTimeForDashFormat("1-13-2016"),"AAPL",223985)
  }
    
   test("get smallest trade for investor") {
    metrics.getSmallestTradeForInvestor(investorData,1) shouldBe InvestorOrder(1,"SELL",DateUtil.toDateTimeForDashFormat("1-12-2016"),"BR",1285)
  }  

    test("get buy and hold investor") {
    metrics.getBuyAndHoldInvestors(investorData) shouldBe Seq(1,2)}
}
