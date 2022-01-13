package com.lantu.woevent.service;

import com.lantu.woevent.models.News;

import java.util.List;

public interface INewsService
{
    public News findNewsByDate(String date);

    public List<News> findAllNews();

    public boolean addNews(News news);

    public boolean deleteNews(String date);

    public boolean updateNews(News news);
}
