package com.example.data.common.mapper

/**
 * Entity(data) -> BaseModel(domain)로 변경하는 인터페이스
 * @param Entity:  내부 DB 테이블에 매핑된 Entity
 * @param BaseModel: Domain 계층에서 사용하는 Dto
 *
 * @return BaseModel
 */
interface EntityMapper<From, To> {
    fun From.entityToDomainModel(): To
    fun To.domainModelToEntity(): From
}