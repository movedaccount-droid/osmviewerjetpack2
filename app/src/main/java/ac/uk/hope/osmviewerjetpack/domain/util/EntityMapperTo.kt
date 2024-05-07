package ac.uk.hope.osmviewerjetpack.domain.util

interface EntityMapperTo <Entity, DomainModel> {
    fun mapToEntity(domainModel: DomainModel): Entity
    fun mapToEntityList(list: List<DomainModel>): List<Entity> {
        return list.map { mapToEntity(it) }
    }
}