package ac.uk.hope.osmviewerjetpack.domain.util;

interface EntityMapperFrom <Entity, DomainModel> {
    fun mapFromEntity(entity: Entity): DomainModel
    fun mapFromEntityList(list: List<Entity>): List<DomainModel> {
        return list.map { mapFromEntity(it) }
    }
}