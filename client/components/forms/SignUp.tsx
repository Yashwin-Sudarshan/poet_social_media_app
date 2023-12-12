"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import * as z from "zod";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "../ui/form";
import { Input } from "../ui/input";
import { Button } from "../ui/button";
import { Checkbox } from "../ui/checkbox";
import { SignUpSchema } from "@/lib/validations";
import { registerUser } from "@/lib/actions/user.action";
import { useRouter } from "next/navigation";

const SignUp = () => {
  const router = useRouter();

  // 1. Define your form.
  const form = useForm<z.infer<typeof SignUpSchema>>({
    resolver: zodResolver(SignUpSchema),
    defaultValues: {
      email: "",
      username: "",
      password: "",
      confirmPassword: "",
    },
  });

  async function onSubmit(values: z.infer<typeof SignUpSchema>) {
    const { username, email, password } = values;
    try {
      const token = await registerUser({ username, email, password });

      document.cookie = `token=${token}; path=/; HttpOnly; Secure`; // Make sure to use HTTPS in production

      router.push("/poems");
    } catch (error: any) {
      console.error("Registration failed:", error.message);
      alert(error.message);
    }
  }

  return (
    <Form {...form}>
      <form
        onSubmit={form.handleSubmit(onSubmit)}
        className="space-y-5 max-[430px]:space-y-[10px]"
      >
        <FormField
          control={form.control}
          name="email"
          render={({ field }) => (
            <FormItem>
              <FormControl>
                <Input
                  placeholder="Email*"
                  {...field}
                  className="h-14 w-full rounded-[5px] border-none bg-brown-textfield
                        text-lg text-gray-dark-textfield outline-none ring-pale
                        placeholder:text-gray-dark-textfield dark:bg-gray-dark-textfield
                        dark:text-pale dark:placeholder:text-pale max-[430px]:h-10
                        max-[430px]:text-sm"
                />
              </FormControl>
              <FormMessage className="pt-1 text-red-500" />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="username"
          render={({ field }) => (
            <FormItem>
              <FormControl>
                <Input
                  placeholder="Username*"
                  {...field}
                  className="h-14 w-full rounded-[5px] border-none bg-brown-textfield
                        text-lg text-gray-dark-textfield outline-none ring-pale
                        placeholder:text-gray-dark-textfield dark:bg-gray-dark-textfield
                        dark:text-pale dark:placeholder:text-pale max-[430px]:h-10
                        max-[430px]:text-sm"
                />
              </FormControl>
              <FormMessage className="pt-1 text-red-500" />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="password"
          render={({ field }) => (
            <FormItem>
              <FormControl>
                <Input
                  placeholder="Password*"
                  type="password"
                  {...field}
                  className="h-14 w-full rounded-[5px] border-none bg-brown-textfield
                        text-lg text-gray-dark-textfield outline-none ring-pale
                        placeholder:text-gray-dark-textfield dark:bg-gray-dark-textfield
                        dark:text-pale dark:placeholder:text-pale max-[430px]:h-10
                        max-[430px]:text-sm"
                />
              </FormControl>
              <FormMessage className="pt-1 text-red-500" />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="confirmPassword"
          render={({ field }) => (
            <FormItem>
              <FormControl>
                <Input
                  placeholder="Confirm password*"
                  type="password"
                  {...field}
                  className="h-14 w-full rounded-[5px] border-none bg-brown-textfield
                        text-lg text-gray-dark-textfield outline-none ring-pale
                        placeholder:text-gray-dark-textfield dark:bg-gray-dark-textfield
                        dark:text-pale dark:placeholder:text-pale max-[430px]:h-10
                        max-[430px]:text-sm"
                />
              </FormControl>
              <FormMessage className="pt-1 text-red-500" />
            </FormItem>
          )}
        />

        <div>
          <FormField
            control={form.control}
            name="termsAndConditions"
            render={({ field }) => (
              <>
                <FormItem className="mt-10 flex align-top max-[430px]:mt-5">
                  <FormControl>
                    <Checkbox
                      checked={field.value}
                      onCheckedChange={field.onChange}
                      className={`h-[18px] w-[18px] border-2 border-brown dark:border-pale
                    ${
                      field.value
                        ? "bg-brown text-pale dark:bg-pale dark:text-gray-dark"
                        : ""
                    }`}
                    />
                  </FormControl>
                  <FormLabel className="pl-2.5 text-base text-brown dark:text-pale">
                    I have read and agree to poetvine&apos;s{" "}
                    <span className="underline">terms and conditions</span>*
                  </FormLabel>
                </FormItem>

                <FormItem>
                  <FormMessage className="text-red-500" />
                </FormItem>
              </>
            )}
          />

          <FormField
            control={form.control}
            name="privacyPolicy"
            render={({ field }) => (
              <>
                <FormItem className="mt-5 flex align-top">
                  <FormControl>
                    <Checkbox
                      checked={field.value}
                      onCheckedChange={field.onChange}
                      className={`h-[18px] w-[18px] border-2 border-brown dark:border-pale
                    ${
                      field.value
                        ? "bg-brown text-pale dark:bg-pale dark:text-gray-dark"
                        : ""
                    }`}
                    />
                  </FormControl>
                  <FormLabel className="pl-2.5 text-base text-brown dark:text-pale">
                    I have read and agree to poetvine&apos;s{" "}
                    <span className="underline">privacy policy</span>*
                  </FormLabel>
                </FormItem>

                <FormItem className="mb-5">
                  <FormMessage className="text-red-500" />
                </FormItem>
              </>
            )}
          />
        </div>

        <Button
          type="submit"
          className="mt-10 h-14 w-full rounded-[10px] bg-brown text-2xl font-bold text-pale
            hover:shadow-default dark:border-dark-pale dark:bg-dark-pale max-[430px]:h-10 max-[430px]:text-lg"
        >
          Sign up
        </Button>
      </form>
    </Form>
  );
};

export default SignUp;
