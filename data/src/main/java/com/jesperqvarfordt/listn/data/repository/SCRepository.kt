package com.jesperqvarfordt.listn.data.repository

import com.jesperqvarfordt.listn.data.api.SCApi
import com.jesperqvarfordt.listn.data.mapper.TrackMapper

abstract class SCRepository(protected val api: SCApi,
                            protected val mapper: TrackMapper)