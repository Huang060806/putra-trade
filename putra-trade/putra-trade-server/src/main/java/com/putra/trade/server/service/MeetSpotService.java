package com.putra.trade.server.service;

import com.putra.trade.common.context.BaseContext;
import com.putra.trade.common.exception.BusinessException;
import com.putra.trade.pojo.dto.MeetSpotDTO;
import com.putra.trade.pojo.entity.MeetSpot;
import com.putra.trade.server.mapper.MeetSpotMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetSpotService {

    private final MeetSpotMapper meetSpotMapper;

    public List<MeetSpot> list() {
        return meetSpotMapper.listByMember(BaseContext.getCurrentId());
    }

    public void add(MeetSpotDTO dto) {
        MeetSpot spot = new MeetSpot();
        BeanUtils.copyProperties(dto, spot);
        spot.setMemberId(BaseContext.getCurrentId());
        if (spot.getIsDefault() != null && spot.getIsDefault() == 1) {
            meetSpotMapper.clearDefault(spot.getMemberId());
        }
        meetSpotMapper.insert(spot);
    }

    public void update(MeetSpotDTO dto) {
        MeetSpot spot = new MeetSpot();
        BeanUtils.copyProperties(dto, spot);
        checkOwner(spot.getId());
        if (spot.getIsDefault() != null && spot.getIsDefault() == 1) {
            meetSpotMapper.clearDefault(BaseContext.getCurrentId());
        }
        meetSpotMapper.update(spot);
    }

    public void delete(Long id) {
        checkOwner(id);
        meetSpotMapper.deleteById(id);
    }

    private void checkOwner(Long id) {
        MeetSpot spot = meetSpotMapper.getById(id);
        if (spot == null || !spot.getMemberId().equals(BaseContext.getCurrentId())) {
            throw new BusinessException("面交地点不存在");
        }
    }
}
