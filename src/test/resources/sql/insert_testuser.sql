INSERT INTO public.users
(username, "password", enabled)
values
('testuser', crypt('testuser', gen_salt('bf', 10)), true);
