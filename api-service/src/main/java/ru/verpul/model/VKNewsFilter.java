package ru.verpul.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "vk_news_filter")
public class VKNewsFilter extends BaseEntity{

    @Column(name = "filter", nullable = false, unique = true)
    private String filter;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VKNewsFilter other = (VKNewsFilter) o;
        return this.filter.equals(other.getFilter());
    }

    @Override
    public int hashCode() {
        return Objects.hash(filter);
    }
}
