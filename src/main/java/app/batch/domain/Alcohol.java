package app.batch.domain;


import app.batch.domain.constant.AlcoholCategoryGroup;
import app.batch.domain.constant.AlcoholType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Entity(name = "alcohol")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alcohol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("알코올 한글 이름")
    @Column(name = "kor_name", nullable = false)
    private String korName;

    @Comment("알코올 영어 이름")
    @Column(name = "eng_name", nullable = false)
    private String engName;

    @Comment("도수")
    @Column(name = "abv")
    private String abv;

    @Comment("타입")
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AlcoholType type;

    @Comment("하위 카테고리 한글명 ( ex. 위스키, 럼 )")
    @Column(name = "kor_category", nullable = false)
    private String korCategory;

    @Comment("하위 카테고리 영문명 ( ex. 위스키, 럼 )")
    @Column(name = "eng_category", nullable = false)
    private String engCategory;

    @Comment("하위 카테고리 그룹")
    @Enumerated(EnumType.STRING)
    @Column(name = "category_group", nullable = false)
    private AlcoholCategoryGroup categoryGroup;

    @Comment("국가")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @Comment("증류소")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "distillery_id")
    private Distillery distillery;

    @Comment("캐스트 타입")
    @Column(name = "cask")
    private String cask;

    @Comment("썸네일 이미지")
    @Column(name = "image_url")
    private String imageUrl;
}
