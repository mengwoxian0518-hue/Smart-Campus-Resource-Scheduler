-- 获取Key
local key = KEYS[1]
-- 获取限流次数
local limit = tonumber(ARGV[1])
-- 获取限流时间
local expire = tonumber(ARGV[2])

-- 获取当前流量
local current = tonumber(redis.call('get', key) or "0")

if current + 1 > limit then
    -- 超过限流
    return 0
else
    -- 没有超过限流
    redis.call("INCRBY", key, 1)
    if current == 0 then
        -- 如果是第一次访问，设置过期时间
        redis.call("EXPIRE", key, expire)
    end
    return 1
end
