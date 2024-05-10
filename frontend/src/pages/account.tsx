import type { User } from "../types";

import {
  Form,
  Grid,
  Alert,
  Label,
  Button,
  Select,
  Fieldset,
  TextInput,
  ButtonGroup,
  GridContainer,
} from "@trussworks/react-uswds";
import { states } from "../states";
import { valueOrNull } from "../lib/utils";
import { FormEvent, useState } from "react";
import { useTranslation } from "react-i18next";
import { useAuth } from "../contexts/auth-context";
import { Navigate, useNavigate } from "react-router-dom";

export default function Account() {
  const navigate = useNavigate();

  const { loading, jwt, user, setUser, username, logout } = useAuth();

  const { t } = useTranslation();

  const [okMsg, setOkMsg] = useState<string | null>(null);
  const [errMsg, setErrMsg] = useState<string | null>(null);

  const handleAccountSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    setErrMsg(null);
    setOkMsg(null);

    const formData = {
      suffix: user?.suffix,
      dateOfBirth: user?.dateOfBirth,
      ssn: user?.ssn,
      phoneNumber: user?.phoneNumber,
      taxReturn: user?.taxReturn,
      // @ts-expect-error untyped form elements but we need the values
      firstName: valueOrNull(e.currentTarget.elements.first_name.value),
      // @ts-expect-error untyped form elements but we need the values
      lastName: valueOrNull(e.currentTarget.elements.last_name.value),
      // @ts-expect-error untyped form elements but we need the values
      email: valueOrNull(e.currentTarget.elements.email.value),
      // @ts-expect-error untyped form elements but we need the values
      streetAddress: valueOrNull(e.currentTarget.elements.street_address.value),
      // @ts-expect-error untyped form elements but we need the values
      city: valueOrNull(e.currentTarget.elements.city.value),
      // @ts-expect-error untyped form elements but we need the values
      userState: valueOrNull(e.currentTarget.elements.state.value, t("select")),
      // @ts-expect-error untyped form elements but we need the values
      zipCode: valueOrNull(e.currentTarget.elements.zipcode.value),
    };

    const res = await fetch(
      `https://api-taxguru.skillstorm-congo.com/users/update?username=${username}`,
      {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${jwt}`,
        },
        body: JSON.stringify(formData),
      }
    );

    if (res.ok) {
      const data: User = await res.json();
      setUser(data);
      setOkMsg("Updated profile");
    } else {
      console.log("Error: ", res.status);
      if (res.headers.get("content-type")?.includes("application/json")) {
        const data = await res.json();
        setErrMsg(data.error);
      } else if (res.headers.get("content-type")?.includes("text/plain")) {
        const text = await res.text();
        setErrMsg(text);
      }
    }

    return;
  };

  const handlePasswordChange = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    return;
  };

  if (loading) {
    return <h1>Loading...</h1>;
  }

  // redirect them to the login page if they are not logged in
  if (!jwt && !loading) {
    return <Navigate to="/login" />;
  }

  return (
    <main className="full-page">
      <div className="bg-base-lightest">
        <GridContainer className="usa-section">
          {okMsg && (
            <Alert type="success" headingLevel="h4" slim>
              Successfully updated profile
            </Alert>
          )}
          {errMsg && (
            <Alert type="error" headingLevel="h4" slim>
              Error updating profile: {errMsg}
            </Alert>
          )}

          <h1>
            {t("account.greeting")}
            {user?.firstName ?? username}
          </h1>

          <Form onSubmit={handleAccountSubmit} className="w-full">
            <Fieldset legend={t("account.desc1")} legendStyle="default">
              {/* First, Last Name, & Email */}
              <Grid row gap>
                <Grid tablet={{ col: true }}>
                  <Label htmlFor="first_name">{t("personal.fname")}</Label>
                  <TextInput
                    id="first_name"
                    name="first_name"
                    type="text"
                    defaultValue={user?.firstName ?? undefined}
                    autoComplete="given-name"
                  />
                </Grid>

                <Grid tablet={{ col: true }}>
                  <Label htmlFor="last_name">{t("personal.lname")}</Label>
                  <TextInput
                    id="last_name"
                    name="last_name"
                    type="text"
                    defaultValue={user?.lastName ?? undefined}
                    autoComplete="family-name"
                  />
                </Grid>

                <Grid tablet={{ col: true }}>
                  <Label htmlFor="email">{t("account.email")}</Label>
                  <TextInput
                    id="email"
                    name="email"
                    type="email"
                    defaultValue={user?.email ?? undefined}
                    autoComplete="email"
                  />
                </Grid>
              </Grid>

              <Grid row gap>
                <Grid tablet={{ col: true }}>
                  <Label htmlFor="street_address">{t("personal.street")}</Label>
                  <TextInput
                    id="street_address"
                    name="street_address"
                    type="text"
                    autoComplete="street-address"
                    defaultValue={user?.streetAddress ?? undefined}
                  />
                </Grid>

                <Grid tablet={{ col: true }}>
                  <Label htmlFor="city">{t("personal.city")}</Label>
                  <TextInput
                    id="city"
                    name="city"
                    type="text"
                    defaultValue={user?.city ?? undefined}
                  />
                </Grid>
              </Grid>

              <Grid row gap>
                <Grid tablet={{ col: true }}>
                  <Label htmlFor="state">{t("personal.state")}</Label>
                  <Select
                    id="state"
                    name="state"
                    defaultValue={user?.userState ?? undefined}
                  >
                    <option>{t("select")}</option>
                    {states.map((s) => (
                      <option key={s} value={s}>
                        {s}
                      </option>
                    ))}
                  </Select>
                </Grid>

                <Grid tablet={{ col: true }}>
                  <Label htmlFor="zipcode">{t("personal.zip")}</Label>
                  <TextInput
                    id="zipcode"
                    name="zipcode"
                    type="text"
                    autoComplete="postal-code"
                    defaultValue={user?.zipCode ?? undefined}
                  />
                </Grid>
              </Grid>

              <ButtonGroup type="default">
                <Button type="submit">{t("account.save")}</Button>
                <Button
                  type="button"
                  onClick={() => {
                    logout();
                    navigate("/");
                  }}
                  secondary
                >
                  {t("logout")}
                </Button>
              </ButtonGroup>
            </Fieldset>
          </Form>
        </GridContainer>
      </div>
      {/* New Password */}
      <GridContainer>
        <h2>{t("account.change")}</h2>

        <Form onSubmit={handlePasswordChange}>
          <Fieldset legend={t("account.desc2")} legendStyle="default">
            <Grid row>
              <Grid tablet={{ col: true }}>
                <Label htmlFor="current-password">{t("account.current")}</Label>
                <TextInput
                  id="current-password"
                  name="current-password"
                  type="password"
                />
              </Grid>
            </Grid>
            <Grid row>
              <Grid tablet={{ col: true }}>
                <Label htmlFor="new-password">{t("account.new")}</Label>
                <TextInput
                  id="new-password"
                  name="new-password"
                  type="password"
                />
              </Grid>
            </Grid>
            <Grid row>
              <Grid tablet={{ col: true }}>
                <Label htmlFor="new-password-confirm">
                  {t("account.confirm")}
                </Label>
                <TextInput
                  id="new-password-confirm"
                  name="new-password-confirm"
                  type="password"
                />
              </Grid>
            </Grid>

            <Button type="submit">{t("account.change-btn")}</Button>
          </Fieldset>
        </Form>
      </GridContainer>
    </main>
  );
}
