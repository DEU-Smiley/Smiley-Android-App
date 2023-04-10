package com.example.data.common.mapper

import com.example.data.common.network.BaseResponse
import com.example.domain.common.base.BaseModel

/**
 * BaseResponse(data) -> BaseModel(domain)로 변경하는 인터페이스
 * @param BaseResponse: Api로 받아오는 Entity
 * @param BaseModel: Domain 계층에서 사용하는 Dto
 *
 * @return BaseModel
 */
interface DataMapper<in R: BaseResponse, out D: BaseModel> {
    fun R.toDomainModel(): D
}