package se.edugrade.dto.subclasses;

public sealed interface ContentType permits ImagePostDTO, LinkPostDTO, TextPostDTO, VideoPostDTO {
}
