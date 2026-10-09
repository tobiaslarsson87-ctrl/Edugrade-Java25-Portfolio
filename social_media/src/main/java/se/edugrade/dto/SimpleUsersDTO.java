package se.edugrade.dto;
import se.edugrade.utility.Colors;

public record SimpleUsersDTO(Long id, String userName) {
    public SimpleUsersDTO {
        if(id == null) throw new IllegalArgumentException("ID can't be empty");
        if(userName.isBlank()) throw new IllegalArgumentException("UserName can't be null or blank");
    }

    @Override
    public String toString() {
        final String G = Colors.rgb(25, 200, 25);
        final String X = Colors.reset();
        StringBuilder sb = new StringBuilder();
        sb.append(G+this.id+X).append(". ").append(this.userName);
        return sb.toString();
    }
}
