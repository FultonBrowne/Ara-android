package com.andromeda.ara.util

import java.util

import com.andromeda.ara.RssFeedModel


class errorMsg {
  def errRssFeedModel(): RssFeedModel = {
    val errRFM = new RssFeedModel("err", "err", "Err", "err")
    errRFM
  }

  def errFullModel(): FullModel = {
    var main1 = util.List[String]
    val errFM = new FullModel("err", "err", "err", "err", "err", main1)
    errFM
  }
}
