package com.lantu.woevent.service.impl;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.lantu.woevent.mapper.INewsMapper;
import com.lantu.woevent.models.News;
import com.lantu.woevent.service.INewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService implements INewsService
{
    @Autowired
    private INewsMapper mapper;

//
//    public NewsService(INewsMapper _mapper)
//    {
//
//    }


    @Override
    public News findNewsByDate(String date)
    {
        News news  = mapper.selectById(date);
        if (news != null)
        {
            return news;
        }
        return null;
    }

    @Override
    public List<News> findAllNews()
    {
        QueryWrapper<News> wrapper = new QueryWrapper<>();
        wrapper.select("news_date","title");
        List<News> list =  mapper.selectList(wrapper);
        return list;
    }

    @Override
    public boolean addNews(News news)
    {
        if (isExist(news.getNewsDate()))
        {
            return false;
        }

        mapper.insert(news);
        return true;
    }

    @Override
    public boolean deleteNews(String date)
    {
        if (isExist(date))
        {
            mapper.deleteById(date);
            return true;
        }

        return false;
    }

    @Override
    public boolean updateNews(News news)
    {
        if (isExist(news.getNewsDate()))
        {
            UpdateWrapper<News> wrapper = new UpdateWrapper<>();
            wrapper.set("title",news.getTitle());

            //如果上传了新闻
            if (news.getContent() != null)
            {
                wrapper.set("content",news.getContent());
            }
            wrapper.eq("news_date",news.getNewsDate());
            mapper.update(news,wrapper);
            return true;
        }
        return false;
    }

    @Override
    public boolean setNewsForAndroid(String date)
    {
        if (!isExist(date))
        {
            return false;
        }


        UpdateWrapper<News> wrapper = new UpdateWrapper<>();
        News news = getOldNewsForAndroid();

        //删除之前设置的安卓新闻
        if (news != null)
        {
            wrapper.eq("news_date",news.getNewsDate()).set("is_for_android",0);
            mapper.update(new News(),wrapper);
        }

        //设置新的安卓新闻
        wrapper.clear();
        wrapper.eq("news_date",date);
        wrapper.set("is_for_android",1);
        mapper.update(new News(),wrapper);

        return true;

    }

    private boolean isExist(String date)
    {
        QueryWrapper<News> wrapper = new QueryWrapper<>();
        wrapper.exists("select news_date from news where news_date = \"" + date + "\"");

        return mapper.exists(wrapper);
    }

    @Override
    public News findNewsForAndroid()
    {
        News news = getOldNewsForAndroid();

        //如果还没有设置，则返回最后一条新闻
        if (news == null)
        {
            Long count = mapper.selectCount(new QueryWrapper<News>());
            count -- ;
            QueryWrapper<News> wrapper = new QueryWrapper<>();
            wrapper.last("limit " + count + ",1" );
            return mapper.selectOne(wrapper);
        }

        return news;
    }

    private News getOldNewsForAndroid()
    {
        QueryWrapper<News> wrapper = new QueryWrapper<>();
        wrapper.eq("is_for_android",1);
        return mapper.selectOne(wrapper);
    }
}
