insert into benutzer(benutzer_id, benutzertyp, vorname, nachname, email, salt, password_hash) values ('juhnke_r', 'native', 'Ralph', 'Juhnke', 'ralph.juhnke@f-i-solutions-plus.de', '123123123', '123123123123');
insert into benutzer(benutzer_id, benutzertyp, vorname, nachname, email, salt, password_hash) values ('demo', 'native', 'Demo', 'Demoname', 'demo.demoname@f-i-solutions-plus.de', '123123123', '123123123123');
insert into benutzer(benutzer_id, benutzertyp, vorname, nachname, email, salt, password_hash) values ('chef', 'native', 'Chef', 'Chefname', 'chef.chefname@f-i-solutions-plus.de', '123123123', '123123123123');
insert into benutzer(benutzer_id, benutzertyp, vorname, nachname, email, salt, password_hash) values ('boss', 'native', 'Boss', 'Bossname', 'boss.bossname@f-i-solutions-plus.de', '123123123', '123123123123');
insert into benutzer_rolle values ('juhnke_r', 'ADMIN');
insert into antrag(id, benutzer_id, antrag_art, antrag_status, von, bis) values (1, 'juhnke_r', 'URLAUB', 'NEU', '2013-11-13', '2013-11-20');
insert into bewilligung (id, antrag_id, position, bewilligungs_status, benutzer_id) values(1, 1, 1, 'BEWILLIGT', 'chef');
insert into bewilligung (id, antrag_id, position, bewilligungs_status, benutzer_id) values(2, 1, 2, 'OFFEN', 'boss');