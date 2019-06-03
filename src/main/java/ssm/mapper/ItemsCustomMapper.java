package ssm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import ssm.po.ItemsCustom;
import ssm.po.ItemsQueryVo;

public interface ItemsCustomMapper {

	public ItemsCustom findItemsById(@Param("id")Integer id);
    public List<ItemsCustom> findItemsList(ItemsQueryVo itemsQueryVo) throws Exception;
}