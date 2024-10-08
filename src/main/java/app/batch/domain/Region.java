package app.batch.domain;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import org.hibernate.annotations.Comment;

@Getter
@Entity(name = "region")
public class Region  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Comment("국가 한글명")
	@Column(name = "kor_name", nullable = false)
	private String korName;

	@Comment("국가 영문명")
	@Column(name = "eng_name", nullable = false)
	private String engName;

	@Comment("대륙")
	@Column(name = "continent", nullable = true)
	private String continent;

	@Comment("지역 설명")
	@Column(name = "description", nullable = true)
	private String description;

}
