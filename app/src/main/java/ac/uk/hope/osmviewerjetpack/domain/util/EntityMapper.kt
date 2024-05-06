package ac.uk.hope.osmviewerjetpack.domain.util

// one of the few cases where i'm gonna say "the guy on youtube did it"
// it's probably a clean code thing. i don't know
// TODO: tidy this up when we inevitably don't need toEntity

interface EntityMapper <Entity, DomainModel> {
    fun mapFromEntity(entity: Entity): DomainModel
    fun mapToEntity(domainModel: DomainModel): Entity
}