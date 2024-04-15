import {
  Grid,
  Form,
  Label,
  Button,
  Fieldset,
  TextInput,
  GridContainer,
} from "@trussworks/react-uswds";
import { Link } from "react-router-dom";
import { FormEvent, useState } from "react";

export default function Login() {
  const [showPassword, setShowPassword] = useState(false);

  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const formData = {
      // @ts-expect-error untyped form elements but we need the values
      email: e.currentTarget.elements.email.value,
      // @ts-expect-error untyped form elements but we need the values
      password: e.currentTarget.elements.password.value,
    };

    console.log(formData);
    e.currentTarget.reset();
  };

  return (
    <main className="full-page">
      <div className="bg-base-lightest">
        <GridContainer className="usa-section">
          <Grid row className="flex-justify-center">
            <Grid col={12} tablet={{ col: 8 }} desktop={{ col: 6 }}>
              <div className="bg-white padding-y-3 padding-x-5 border border-base-lightest">
                <h1 className="margin-bottom-0">Log in</h1>
                <Form onSubmit={handleSubmit}>
                  <Fieldset legend="Access your account" legendStyle="default">
                    <Label htmlFor="email">Email address</Label>
                    <TextInput id="email" name="email" type="email" required />

                    <Label htmlFor="password">Password</Label>
                    <TextInput
                      id="password"
                      name="password"
                      type={showPassword ? "text" : "password"}
                    />

                    <button
                      title="Toggle Password Visibility"
                      type="button"
                      className="usa-show-password"
                      aria-controls="password password_confirm"
                      onClick={() => setShowPassword((prev) => !prev)}
                    >
                      {showPassword ? "Hide Password" : "Show Password"}
                    </button>

                    <Button type="submit">Log in</Button>
                  </Fieldset>
                </Form>
              </div>

              <p className="text-center">
                Don&apos;t have an account?{" "}
                <Link to="/signup" className="usa-link">
                  Create your account now
                </Link>
              </p>
            </Grid>
          </Grid>
        </GridContainer>
      </div>
    </main>
  );
}
