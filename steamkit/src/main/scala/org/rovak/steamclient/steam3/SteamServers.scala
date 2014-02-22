package org.rovak.steamclient.steam3

import scala.util.Random
import steam.Server

object SteamServers {

  val notsupported = List(
    // Starhub, Singapore (non-optimal route)
    Server("103.28.54.10", 27017 ),
    //* Highwinds, Netherlands (not live)
    Server("81.171.115.5", 27017 ),
    Server("81.171.115.5", 27018 ),
    Server("81.171.115.5", 27019 ),
    Server("81.171.115.6", 27017 ),
    Server("81.171.115.6", 27018 ),
    Server("81.171.115.6", 27019 ),
    Server("81.171.115.7", 27017 ),
    Server("81.171.115.7", 27018 ),
    Server("81.171.115.7", 27019 ),
    Server("81.171.115.8", 27017 ),
    Server("81.171.115.8", 27018 ),
    Server("81.171.115.8", 27019 ))

  val recommended = List(
  Server("72.165.61.174", 27017),
  Server("72.165.61.174", 27018),
  Server("72.165.61.175", 27017),
  Server("72.165.61.175", 27018),
  Server("72.165.61.176", 27017),
  Server("72.165.61.176", 27018),
  Server("72.165.61.185", 27017),
  Server("72.165.61.185", 27018),
  Server("72.165.61.187", 27017),
  Server("72.165.61.187", 27018),
  Server("72.165.61.188", 27017),
  Server("72.165.61.188", 27018),
  // Inteliquent, Luxembourg, cm-[01-04].lux.valve.net
  Server("146.66.152.12", 27017),
  Server("146.66.152.12", 27018),
  Server("146.66.152.12", 27019),
  Server("146.66.152.13", 27017),
  Server("146.66.152.13", 27018),
  Server("146.66.152.13", 27019),
  Server("146.66.152.14", 27017),
  Server("146.66.152.14", 27018),
  Server("146.66.152.14", 27019),
  Server("146.66.152.15", 27017),
  Server("146.66.152.15", 27018),
  Server("146.66.152.15", 27019),
  /* Highwinds, Netherlands (not live)
  Server("81.171.115.5", 27017),
  Server("81.171.115.5", 27018),
  Server("81.171.115.5", 27019),
  Server("81.171.115.6", 27017),
  Server("81.171.115.6", 27018),
  Server("81.171.115.6", 27019),
  Server("81.171.115.7", 27017),
  Server("81.171.115.7", 27018),
  Server("81.171.115.7", 27019),
  Server("81.171.115.8", 27017),
  Server("81.171.115.8", 27018),
  Server("81.171.115.8", 27019),*/
  // Highwinds, Kaysville
  Server("209.197.29.196", 27017),
  Server("209.197.29.197", 27017)
  )

  val all = recommended ++ notsupported

  def random = Random.shuffle(recommended).head
}
