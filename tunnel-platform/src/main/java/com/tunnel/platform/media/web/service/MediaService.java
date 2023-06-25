package com.tunnel.platform.media.web.service;

import com.tunnel.platform.media.web.domain.MediaStream;

import java.util.List;

public interface MediaService {
    public List<MediaStream> getList();
    public boolean add(MediaStream mediaStream);
    public MediaStream getStream(MediaStream mediaStream);
}
