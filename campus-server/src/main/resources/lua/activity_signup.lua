-- Lua脚本：活动报名扣减库存
-- KEYS[1]: 库存Key "activity:stock:{id}"
-- KEYS[2]: 用户已报名Set Key "activity:signup:users:{id}"
-- ARGV[1]: 用户ID
-- ARGV[2]: 活动结束时间戳(秒)，用于设置Set过期时间

local stockKey = KEYS[1]
local userSetKey = KEYS[2]
local userId = ARGV[1]
local expireTime = tonumber(ARGV[2])

-- 1. 检查是否已报名
if redis.call('SISMEMBER', userSetKey, userId) == 1 then
    return -1 -- 已报名
end

-- 2. 检查库存是否存在
if redis.call('EXISTS', stockKey) == 0 then
    return -2 -- 活动未上架或库存未初始化
end

-- 3. 检查库存是否充足
local stock = tonumber(redis.call('GET', stockKey))
if stock <= 0 then
    return 0 -- 库存不足
end

-- 4. 扣减库存
redis.call('DECR', stockKey)

-- 5. 记录用户已报名
redis.call('SADD', userSetKey, userId)

-- 6. 设置过期时间（为了防止Set无限膨胀，设置为活动结束后一段时间过期）
-- 注意：Set的过期时间只能整体设置，不能针对单个Member
-- 这里只在Set首次创建时或每次都尝试刷新过期时间
if expireTime > 0 then
    redis.call('EXPIREAT', userSetKey, expireTime)
end

return 1 -- 报名成功
