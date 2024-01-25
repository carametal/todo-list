INSERT INTO public.users
(username, "password", enabled)
values
('testadmin', crypt('testadmin', gen_salt('bf', 10)), true),
('testuser', crypt('testuser', gen_salt('bf', 10)), true);

INSERT INTO public.authorities
(user_id, authority)
values
(1, 'ADMIN'),
(2, 'USER');
