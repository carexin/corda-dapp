package ele.database.mapper

import ele.dapp.flow.DistributorDTO
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface DistributorMapper {

    @Select("select * from m_distributor")
    fun listDistributor(): List<DistributorDTO>

    @Select("select * from m_distributor where id = #{id}")
    fun findById(id: Int): DistributorDTO

}