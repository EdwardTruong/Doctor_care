package com.example.doctorcare.domain.file;

import org.springframework.content.commons.annotations.ContentId;
import org.springframework.content.commons.annotations.ContentLength;
import org.springframework.content.commons.annotations.MimeType;
import org.springframework.content.commons.annotations.OriginalFileName;

import com.example.doctorcare.core.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "filestore")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = true)
public class FileStore extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ContentId
    @Column(name = "content_id", nullable=false)
    private String contentId;

    @ContentLength
    @Column(name = "content_length", nullable=false)
    private long contentLength;

    @MimeType
    @Column(name = "content_type", nullable=false)
    private String contentType;
    
    @OriginalFileName
    @Column(name="content_original_filename", length = 200)
    private String contentOriginalFilename;

    @Column(name="content_path", length = 1000)
    private String contentPath;

    @Column(name="is_publish", nullable = false) 
    private boolean publish;
    
}
