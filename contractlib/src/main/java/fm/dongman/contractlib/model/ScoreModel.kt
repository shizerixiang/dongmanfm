package fm.dongman.contractlib.model

/**
 * 评分模型
 * Created by shize on 2017/10/17.
 */
class ScoreModel {
    var comicId: String? = null // 评论的动漫 id
    var userId: String? = null // 评论的用户 id
    var plot: Int = 0 // 剧情
    var style: Int = 0 // 画风
    var music: Int = 0 // 音乐
    var characters: Int = 0 // 人设
    var total: Int = 0 // 总分
}