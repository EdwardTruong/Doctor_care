package com.example.doctorcare.domain.system.permission;

import java.util.Objects;
import com.example.doctorcare.core.domain.BaseEntity;
import com.example.doctorcare.core.enums.Action;
import com.example.doctorcare.core.enums.Resource;
import com.example.doctorcare.core.enums.Scope;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "permissions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"parent"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Permission extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name; // System-generated code, e.g., EDIT:ENTERPRISE:OWN

    @Column(name = "display_name")
    private String displayName; // User-friendly, editable name

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false, length = 30, columnDefinition = "VARCHAR(30)")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Action action; // e.g., READ, CREATE

    @Enumerated(EnumType.STRING)
    @Column(name = "resource", nullable = false, length = 100, columnDefinition = "VARCHAR(100)")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Resource resource; // e.g., DOCUMENT, USER

    @Enumerated(EnumType.STRING)
    @Column(name = "scope", nullable = false, length = 50, columnDefinition = "VARCHAR(50)")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Scope scope; // e.g., OWN, DEPARTMENT

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = true)
    private Permission parent;

    public Permission(Action action, Resource resource, Scope scope) {
        if (action == null) {
            throw new IllegalArgumentException("Action cannot be null");
        }
        if (resource == null) {
            throw new IllegalArgumentException("Resource cannot be null");
        }
        this.action = action;
        this.resource = resource;
        // If scope is null, default to Scope.ANY
        this.scope = (scope == null) ? Scope.ANY : scope;
        this.name = String.format("%s:%s:%s", action.name(), resource.name(), this.scope.name());
    }

    public Permission(Action action, Resource resource,Scope scope, Permission parent) {
        this(action, resource, scope);
        this.parent = parent;
    }

    public static Permission of(Action action, Resource resource, Scope scope) {
        return new Permission(action, resource, scope);
    }


    /**
     * Checks if this permission is applicable to a specific resource.
     * @param resource The resource to check against.
     * @return true if the resource matches, false otherwise.
     */
    public boolean isApplicableToResource(Resource resource) {
        return this.resource == resource;
    }
    
    /**
     * Checks if this permission is applicable to a specific action.
     * @param action The action to check against.
     * @return true if the action matches, false otherwise.
     */
    public boolean isApplicableToAction(Action action) {
        return this.action == action;
    }

    /**
     * Checks if this permission is applicable to a specific scope.
     * @param scope The scope to check against.
     * @return true if the scope matches, false otherwise.
     */
    public boolean isApplicableToScope(Scope scope) {
        return this.scope == scope;
    }

    /**
     * Checks if this permission is applicable to a specific action, resource, and scope.
     * @param action The action to check against.
     * @param resource The resource to check against.
     * @param scope The scope to check against.
     * @return true if all match, false otherwise.
     */
    public boolean isApplicableTo(Action action, Resource resource, Scope scope) {
        return this.action == action && this.resource == resource && this.scope == scope; // Direct comparison for enums is fine
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        // For new, unsaved entities, rely on business key. For persisted entities, ID is best.
        if (id == null || that.id == null) {
            return Objects.equals(name, that.name);
        }
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name); // Name is a unique business key
    }
}