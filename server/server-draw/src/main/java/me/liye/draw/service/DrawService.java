package me.liye.draw.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.liye.draw.dao.DrawMapper;
import me.liye.draw.domain.Draw;
import me.liye.draw.domain.param.CreateDrawParam;
import me.liye.draw.domain.param.ListDrawParam;
import me.liye.open.share.util.TypeConvertor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liye on 2025-09-19.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DrawService {
    final DrawMapper drawMapper;

    public Draw create(CreateDrawParam param) {
        Draw row = TypeConvertor.convert(param, Draw.class);
        drawMapper.insert(row);
        return drawMapper.selectById(row);
    }

    public List<Draw> list(ListDrawParam param) {
        return drawMapper.list(param);
    }
}
