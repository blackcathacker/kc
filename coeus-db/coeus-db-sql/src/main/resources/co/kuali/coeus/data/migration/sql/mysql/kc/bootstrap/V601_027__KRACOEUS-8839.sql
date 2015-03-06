alter table narrative_attachment 
	add foreign key narrative_attachment_fk1 (file_data_id) references file_data (id);

alter table eps_prop_person_bio_attachment 
	add foreign key eps_prop_person_bio_attach_fk1 (file_data_id) references file_data (id);
	
