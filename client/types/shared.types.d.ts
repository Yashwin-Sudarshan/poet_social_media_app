export interface GetPoemsByFilterParams {
  searchQuery?: string;
  filter?: string;
  page?: number;
  pageSize?: number;
}

export interface CreateUserParams {
  username: string;
  email: string;
  password: string;
}
