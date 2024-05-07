import {
  Grid,
  Form,
  Icon,
  Alert,
  Label,
  Button,
  IconList,
  Fieldset,
  TextInput,
  IconListItem,
  IconListIcon,
  GridContainer,
  IconListTitle,
  IconListContent,
} from "@trussworks/react-uswds";
import { FormEvent, useState } from "react";
import { useTranslation } from "react-i18next";
import { Link, useNavigate } from "react-router-dom";

export default function SignUp() {
  const navigate = useNavigate();

  const { t } = useTranslation();

  const [error, setError] = useState<string | null>(null);
  const [showPassword, setShowPassword] = useState(false);

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError(null);

    const formData = {
      // @ts-expect-error untyped form elements but we need the values
      username: e.currentTarget.elements.username.value,
      // @ts-expect-error untyped form elements but we need the values
      password: e.currentTarget.elements.password.value,
      // @ts-expect-error untyped form elements but we need the values
      password_confirm: e.currentTarget.elements.password_confirm.value,
    };

    // check that passwords match
    if (formData.password !== formData.password_confirm) {
      setError("Passwords do not match");
      return;
    }

    const res = await fetch("http://localhost:8080/auth/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        username: formData.username,
        password: formData.password,
      }),
    });

    if (res.ok) {
      // no need to get the response json since we need the user to log in again
      navigate("/login");
    } else {
      if (res.headers.get("content-type")?.includes("application/json")) {
        const data = await res.json();
        setError(data.error);
      } else if (res.headers.get("content-type")?.includes("text/plain")) {
        const text = await res.text();
        setError(text);
      }
    }
  };

  return (
    <main className="full-page">
      <div className="bg-base-lightest">
        <GridContainer className="usa-section">
          {/* Error Alert */}
          <Grid row className="flex-justify-center margin-bottom-205">
            <Grid col={12}>
              {error && (
                <Alert
                  type="error"
                  heading="Error Logging In"
                  headingLevel="h4"
                >
                  {error}
                </Alert>
              )}
            </Grid>
          </Grid>

          <Grid row className="margin-x-neg-205 flex-justify-center">
            <Grid
              col={12}
              mobileLg={{ col: 10 }}
              tablet={{ col: 8 }}
              desktop={{ col: 6 }}
              className="padding-x-205 margin-bottom-4"
            >
              {/* Mobile only, tagline displays at the top of the screen */}
              <h1 className="desktop:display-none font-sans-lg margin-bottom-4 tablet:margin-top-neg-3">
                {t("auth.tagline")}
              </h1>

              <div className="bg-white padding-y-3 padding-x-5 border border-base-lighter">
                <h2 className="margin-bottom-0">{t("auth.signup")}</h2>
                <Form onSubmit={handleSubmit}>
                  <Fieldset legend={t("auth.signup-desc")}>
                    <p>
                      <abbr
                        title="required"
                        className="usa-hint usa-hint--required"
                      >
                        *
                      </abbr>{" "}
                      {t("auth.required")}
                    </p>

                    <Label htmlFor="username" requiredMarker>
                      {t("auth.username")}
                    </Label>
                    <TextInput
                      id="username"
                      name="username"
                      type="text"
                      required
                    />

                    <Label htmlFor="password" requiredMarker>
                      {t("auth.password")}
                    </Label>
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
                      {showPassword
                        ? t("auth.hide-password")
                        : t("auth.show-password")}
                    </button>

                    <Label htmlFor="password" requiredMarker>
                      {t("auth.confirm-password")}
                    </Label>
                    <TextInput
                      id="password_confirm"
                      name="password_confirm"
                      type={showPassword ? "text" : "password"}
                    />

                    <Button type="submit">{t("auth.signup")}</Button>
                  </Fieldset>
                </Form>
              </div>

              <p className="text-center">
                {t("auth.existing-account")}{" "}
                <Link to="/login" className="usa-link">
                  {t("auth.login") + " →"}
                </Link>
              </p>
            </Grid>

            {/* Extra information */}
            <Grid
              col={12}
              mobileLg={{ col: 10 }}
              tablet={{ col: 8 }}
              desktop={{ col: 6 }}
              className="padding-x-205"
            >
              <div className="border-top border-base-lighter padding-top-4 desktop:border-0 desktop:padding-top-0">
                <h2 className="display-none desktop:display-block">
                  {t("auth.tagline")}
                </h2>

                <div className="usa-prose">
                  <p>{t("auth.tagline-desc")}</p>
                  <IconList className="usa-icon-list--size-lg">
                    <IconListItem>
                      <IconListIcon className="text-green">
                        <Icon.AttachMoney />
                      </IconListIcon>
                      <IconListContent>
                        <IconListTitle type="h3">
                          {t("auth.point-1")}
                        </IconListTitle>
                        <p>{t("auth.point-1-desc")}</p>
                      </IconListContent>
                    </IconListItem>

                    <IconListItem>
                      <IconListIcon className="text-green">
                        <Icon.Shield />
                      </IconListIcon>
                      <IconListContent>
                        <IconListTitle type="h3">
                          {t("auth.point-2")}
                        </IconListTitle>
                        <p>{t("auth.point-2-desc")}</p>
                      </IconListContent>
                    </IconListItem>
                  </IconList>
                </div>
              </div>
            </Grid>
          </Grid>
        </GridContainer>
      </div>
    </main>
  );
}
