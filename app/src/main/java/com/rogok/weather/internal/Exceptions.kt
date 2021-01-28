package com.rogok.weather.internal

import java.io.IOException
import java.lang.Exception

class NoConnectivityException: IOException()
class LocationPermissionNotGrantedException: Exception()